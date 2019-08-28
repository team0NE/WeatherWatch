package on.team.weatherwatch.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import on.team.weatherwatch.data.network.response.CurrentWeatherResponse
import on.team.weatherwatch.data.network.response.FutureWeatherResponse
import on.team.weatherwatch.utils.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val apixuWeatherApiService: ApixuWeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchCurrentWeather = apixuWeatherApiService
                .getCurrentWeather(location, languageCode)
                .await()
            _downloadedCurrentWeather.postValue(fetchCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    override suspend fun fetchFutureWeather(location: String, days: Int, languageCode: String) {
        try {
            val fetchFutureWeather = apixuWeatherApiService
                .getFutureWeather(location, days, languageCode)
                .await()
            _downloadedFutureWeather.postValue(fetchFutureWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}