package on.team.weatherium

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import on.team.weatherium.data.db.FutureWeatherDataBase
import on.team.weatherium.data.network.*
import on.team.weatherium.data.provider.LocationProvider
import on.team.weatherium.data.provider.LocationProviderImpl
import on.team.weatherium.data.provider.UnitProvider
import on.team.weatherium.data.provider.UnitProviderImpl
import on.team.weatherium.data.repository.FutureWeatherRepository
import on.team.weatherium.data.repository.FutureWeatherRepositoryImpl
import on.team.weatherium.ui.weather.current.CurrentWeatherViewModelFactory
import on.team.weatherium.ui.weather.future.detail.FutureDetailedViewModelFactory
import on.team.weatherium.ui.weather.future.list.FutureListViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class WeatheriumApp : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@WeatheriumApp))

        bind() from singleton { FutureWeatherDataBase(instance()) } //this instance means context from androidXModule
        bind() from singleton { instance<FutureWeatherDataBase>().currentWeatherDAO() }
        bind() from singleton { instance<FutureWeatherDataBase>().futureWeatherDAO() }
        bind() from singleton { instance<FutureWeatherDataBase>().weatherLocationDAO() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }

        bind() from singleton { ApixuWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        //FusedLocationProviderClient dependency
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<FutureWeatherRepository>() with singleton {
            FutureWeatherRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        // bind CurrentVM factory
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        // bind FutureVM factory
        bind() from provider { FutureListViewModelFactory(instance(), instance()) }
        //bind FutureDetailedVM
        bind() from factory { detailDate: LocalDate ->
            FutureDetailedViewModelFactory(
                detailDate,
                instance(),
                instance()
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        //This set prefs from preferences.xml fields
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}