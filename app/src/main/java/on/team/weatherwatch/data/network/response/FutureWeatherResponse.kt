package on.team.weatherwatch.data.network.response


import com.google.gson.annotations.SerializedName
import on.team.weatherwatch.data.db.entity.WeatherLocation

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val futureWeatherEntries: FutureWeatherDaysContainer,
    val location: WeatherLocation
)