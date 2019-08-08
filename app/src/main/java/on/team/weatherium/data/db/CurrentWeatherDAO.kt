package on.team.weatherium.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import on.team.weatherium.data.db.entity.CurrentWeatherEntry
import on.team.weatherium.data.db.unitlocalized.current.ImperialCurrentWeatherEntry
import on.team.weatherium.data.db.unitlocalized.current.MetricCurrentWeatherEntry
import on.team.weatherium.utils.CURRENT_WEATHER_ID

@Dao
interface CurrentWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<MetricCurrentWeatherEntry>

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<ImperialCurrentWeatherEntry>
}