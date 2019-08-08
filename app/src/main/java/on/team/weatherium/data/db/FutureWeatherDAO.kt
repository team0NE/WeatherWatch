package on.team.weatherium.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import on.team.weatherium.data.db.entity.FutureWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.detailed.ImperialDetailedFutureWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.detailed.MetricDetailedFutureWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.list.ImperialSimpleFutureWeatherEntry
import on.team.weatherium.data.db.unitlocalized.future.list.MetricSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("select * from future_weather where date(date) >= date(:startDate)")
    fun getSimpleFutureWeatherMetric(startDate: LocalDate): LiveData<List<MetricSimpleFutureWeatherEntry>>

    @Query("select * from future_weather where date(date) >= date(:startDate)")
    fun getSimpleFutureWeatherImperial(startDate: LocalDate): LiveData<List<ImperialSimpleFutureWeatherEntry>>

    @Query("select * from future_weather where date(date) = date(:date)")
    fun getDetailedWeatherByDateMetric(date: LocalDate): LiveData<MetricDetailedFutureWeatherEntry>

    @Query("select * from future_weather where date(date) = date(:date)")
    fun getDetailedWeatherByDateImperial(date: LocalDate): LiveData<ImperialDetailedFutureWeatherEntry>

    @Query("select count(id) from future_weather where date(date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("delete from future_weather where date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)
}