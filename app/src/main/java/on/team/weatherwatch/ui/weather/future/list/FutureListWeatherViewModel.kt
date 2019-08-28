package on.team.weatherwatch.ui.weather.future.list

import on.team.weatherwatch.data.provider.UnitProvider
import on.team.weatherwatch.data.repository.FutureWeatherRepository
import on.team.weatherwatch.ui.base.WeatherViewModel
import on.team.weatherwatch.utils.lazyDeferred
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val futureWeatherRepository: FutureWeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(futureWeatherRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        futureWeatherRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }
}
