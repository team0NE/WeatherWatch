package on.team.weatherium.data.network.response

import com.google.gson.annotations.SerializedName
import on.team.weatherium.data.db.entity.CurrentWeatherEntry
import on.team.weatherium.data.db.entity.WeatherLocation


data class CurrentWeatherResponse(
    val location: WeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)
