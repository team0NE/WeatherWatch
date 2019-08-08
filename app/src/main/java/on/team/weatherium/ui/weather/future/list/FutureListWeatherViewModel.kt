package on.team.weatherium.ui.weather.future.list

import on.team.weatherium.data.provider.UnitProvider
import on.team.weatherium.data.repository.FutureWeatherRepository
import on.team.weatherium.ui.base.WeatherViewModel
import on.team.weatherium.utils.lazyDeferred
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val futureWeatherRepository: FutureWeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(futureWeatherRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        futureWeatherRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }
}
