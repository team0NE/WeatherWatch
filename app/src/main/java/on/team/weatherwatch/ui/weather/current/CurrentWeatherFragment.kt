package on.team.weatherwatch.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import on.team.weatherwatch.R
import on.team.weatherwatch.ui.base.ScopedFragment
import on.team.weatherwatch.utils.glide.GlideApp
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        buildUI()
    }

    private fun buildUI() = launch {
        val currentWeather = viewModel.weather.await()
        val currentLocation = viewModel.weatherLocation.await()

        currentLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            updateDateToToday()
            updateTemperatures(it.temperature, it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)

            GlideApp.with(this@CurrentWeatherFragment)
                .load("http:${it.conditionIconUrl}")
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUniteAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLike: Double) {
        val uniteAbbreviation = chooseLocalizedUniteAbbreviation("°C", "°F")
        textView_temperature.text = "$temperature$uniteAbbreviation"
        textView_feels_like_temperature.text = "Feels like $feelsLike$uniteAbbreviation"
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val uniteAbbreviation = chooseLocalizedUniteAbbreviation("mm", "in")
        textView_precipitation.text = "Precipitation: $precipitationVolume $uniteAbbreviation"
    }

    private fun updateWind(winDirection: String, windSpeed: Double) {
        val uniteAbbreviation = chooseLocalizedUniteAbbreviation("kph", "mph")
        textView_wind.text = "Wind: $winDirection, $windSpeed $uniteAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val uniteAbbreviation = chooseLocalizedUniteAbbreviation("km", "ml")
        textView_visibility.text = "Visibility: $visibilityDistance $uniteAbbreviation"
    }
}
