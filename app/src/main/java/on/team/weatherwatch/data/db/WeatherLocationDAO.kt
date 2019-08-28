package on.team.weatherwatch.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import on.team.weatherwatch.data.db.entity.WeatherLocation
import on.team.weatherwatch.utils.WEATHER_LOCATION_ID

@Dao
interface WeatherLocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInsert(weatherLocation: WeatherLocation)

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<WeatherLocation>

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getNotLiveDataLocation(): WeatherLocation?
}