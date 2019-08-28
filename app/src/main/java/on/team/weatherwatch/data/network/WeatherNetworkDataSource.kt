package on.team.weatherwatch.data.network

import androidx.lifecycle.LiveData
import on.team.weatherwatch.data.network.response.CurrentWeatherResponse
import on.team.weatherwatch.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {

    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        location: String,
        days: Int,
        languageCode: String
    )
}