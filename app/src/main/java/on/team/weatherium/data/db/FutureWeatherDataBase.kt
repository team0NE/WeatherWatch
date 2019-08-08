package on.team.weatherium.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import on.team.weatherium.data.db.entity.CurrentWeatherEntry
import on.team.weatherium.data.db.entity.FutureWeatherEntry
import on.team.weatherium.data.db.entity.WeatherLocation
import on.team.weatherium.utils.LocalDateConverter

@Database(
    entities = [CurrentWeatherEntry::class, FutureWeatherEntry::class, WeatherLocation::class],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
abstract class FutureWeatherDataBase : RoomDatabase() {
    abstract fun currentWeatherDAO(): CurrentWeatherDAO
    abstract fun futureWeatherDAO(): FutureWeatherDAO
    abstract fun weatherLocationDAO(): WeatherLocationDAO

    companion object {
        @Volatile
        private var instance: FutureWeatherDataBase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FutureWeatherDataBase::class.java,
                "future_weather.db"
            ).build()
    }
}