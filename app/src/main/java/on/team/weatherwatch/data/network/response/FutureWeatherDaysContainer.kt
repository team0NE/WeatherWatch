package on.team.weatherwatch.data.network.response

import com.google.gson.annotations.SerializedName
import on.team.weatherwatch.data.db.entity.FutureWeatherEntry

data class FutureWeatherDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)