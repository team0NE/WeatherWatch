package on.team.weatherium.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import on.team.weatherium.data.db.CurrentWeatherDAO
import on.team.weatherium.data.db.FutureWeatherDAO
import on.team.weatherium.data.db.WeatherLocationDAO
import on.team.weatherium.data.db.entity.WeatherLocation
import on.team.weatherium.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.detailed.UnitSpecificDetailedFutureWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import on.team.weatherium.data.network.WeatherNetworkDataSource
import on.team.weatherium.data.network.response.CurrentWeatherResponse
import on.team.weatherium.data.network.response.FutureWeatherResponse
import on.team.weatherium.data.provider.LocationProvider
import on.team.weatherium.utils.FUTURE_DAYS_COUNT
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

class FutureWeatherRepositoryImpl(
    private val currentWeatherDAO: CurrentWeatherDAO,
    private val futureWeatherDAO: FutureWeatherDAO,
    private val weatherLocationDAO: WeatherLocationDAO,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : FutureWeatherRepository {

    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) currentWeatherDAO.getWeatherMetric()
            else currentWeatherDAO.getWeatherImperial()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        isMetric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()

            return@withContext if (isMetric) {
                futureWeatherDAO.getSimpleFutureWeatherMetric(startDate)
            } else futureWeatherDAO.getSimpleFutureWeatherImperial(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        isMetric: Boolean
    ): LiveData<out UnitSpecificDetailedFutureWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (isMetric) futureWeatherDAO.getDetailedWeatherByDateMetric(date)
            else futureWeatherDAO.getDetailedWeatherByDateImperial(date)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDAO.addInsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDAO.addInsert(fetchedWeather.location)
        }
    }

    private fun persistFetchedFutureWeather(newFutureWeather: FutureWeatherResponse) {
        fun deleteOldWeatherData() {
            val today = LocalDate.now()
            futureWeatherDAO.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldWeatherData()
            val futureWeatherList = newFutureWeather.futureWeatherEntries.entries
            futureWeatherDAO.insert(futureWeatherList)
            weatherLocationDAO.addInsert(newFutureWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDAO.getNotLiveDataLocation()

        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }
        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()
        if (isFetchFutureNeeded())
            fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(),
            FUTURE_DAYS_COUNT,
            Locale.getDefault().language
        )
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDAO.countFutureWeather(today)

        return futureWeatherCount < FUTURE_DAYS_COUNT
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDAO.getLocation()
        }
    }
}