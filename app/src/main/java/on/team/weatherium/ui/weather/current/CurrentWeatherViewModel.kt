package on.team.weatherium.ui.weather.current

import on.team.weatherium.data.provider.UnitProvider
import on.team.weatherium.data.repository.FutureWeatherRepository
import on.team.weatherium.ui.base.WeatherViewModel
import on.team.weatherium.utils.lazyDeferred

class CurrentWeatherViewModel(
    private val futureWeatherRepository: FutureWeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(futureWeatherRepository, unitProvider) {

    val weather by lazyDeferred {
        futureWeatherRepository.getCurrentWeather(super.isMetricUnit)
    }
}
