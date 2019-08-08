package on.team.weatherium.data.network.response

import com.google.gson.annotations.SerializedName
import on.team.weatherium.data.db.entity.FutureWeatherEntry

data class FutureWeatherDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)