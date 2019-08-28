package on.team.weatherwatch.data.network.response

import com.google.gson.annotations.SerializedName
import on.team.weatherwatch.data.db.entity.CurrentWeatherEntry
import on.team.weatherwatch.data.db.entity.WeatherLocation


data class CurrentWeatherResponse(
    val location: WeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)
