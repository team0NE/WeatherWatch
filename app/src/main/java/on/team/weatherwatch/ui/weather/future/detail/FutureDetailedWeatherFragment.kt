package on.team.weatherwatch.ui.weather.future.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import on.team.weatherwatch.R
import on.team.weatherwatch.ui.base.ScopedFragment
import on.team.weatherwatch.utils.DateNotFoundException
import on.team.weatherwatch.utils.LocalDateConverter
import on.team.weatherwatch.utils.glide.GlideApp
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureDetailedWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactoryInstanceFactory
            : ((LocalDate) -> FutureDetailedViewModelFactory) by factory()
    private lateinit var viewModel: FutureDetailedWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val takeSavedArgs = arguments?.let {
            FutureDetailedWeatherFragmentArgs.fromBundle(it)
        }
        val passedDate = LocalDateConverter.stringToDate(takeSavedArgs?.dateString)
            ?: throw DateNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(passedDate))
            .get(FutureDetailedWeatherViewModel::class.java)
        buildUI()
    }

    private fun buildUI() = launch(Dispatchers.Main) {
        val detailedWeather = viewModel.detailedWeatherEntry.await()
        val detailedWeatherLocation = viewModel.weatherLocation.await()
        detailed_group_loading.visibility = View.GONE

        detailedWeatherLocation.observe(this@FutureDetailedWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })
        detailedWeather.observe(this@FutureDetailedWeatherFragment, Observer { weather ->
            if (weather == null) return@Observer

            updateDate(weather.date)
            updateTemperatures(weather.avgTemperature, weather.minTemperature, weather.maxTemperature)
            updateCondition(weather.conditionText)
            updatePrecipitation(weather.totalPrecipitation)
            updateWindSpeed(weather.windMaxSpeed)
            updateVisibility(weather.avgVisibilityDistance)
            updateUV(weather.uv)

            GlideApp.with(this@FutureDetailedWeatherFragment)
                .load("http:" + weather.conditionIconUrl)
                .into(detailed_imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(date: LocalDate) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        detailed_textView_temperature.text = "$temperature$unitAbbreviation"
        detailed_textView_min_max_temperature.text = "Min: $min$unitAbbreviation, Max: $max$unitAbbreviation"
    }

    private fun updateCondition(condition: String) {
        detailed_textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        detailed_textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
    }

    private fun updateWindSpeed(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        detailed_textView_wind.text = "Wind speed: $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        detailed_textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }

    private fun updateUV(uv: Double) {
        detailed_textView_uv.text = "UV: $uv"
    }
}
