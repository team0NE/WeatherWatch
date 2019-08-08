package on.team.weatherium.data.network.response


import com.google.gson.annotations.SerializedName
import on.team.weatherium.data.db.entity.WeatherLocation

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val futureWeatherEntries: FutureWeatherDaysContainer,
    val location: WeatherLocation
)