package on.team.weatherium.data.repository

import androidx.lifecycle.LiveData
import on.team.weatherium.data.db.entity.WeatherLocation
import on.team.weatherium.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.detailed.UnitSpecificDetailedFutureWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate


interface FutureWeatherRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getFutureWeatherList(
        startDate: LocalDate,
        isMetric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>

    suspend fun getFutureWeatherByDate(
        date: LocalDate,
        isMetric: Boolean
    ): LiveData<out UnitSpecificDetailedFutureWeatherEntry>

    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}