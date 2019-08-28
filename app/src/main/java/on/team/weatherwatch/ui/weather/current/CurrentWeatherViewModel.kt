package on.team.weatherwatch.ui.weather.current

import on.team.weatherwatch.data.provider.UnitProvider
import on.team.weatherwatch.data.repository.FutureWeatherRepository
import on.team.weatherwatch.ui.base.WeatherViewModel
import on.team.weatherwatch.utils.lazyDeferred

class CurrentWeatherViewModel(
    private val futureWeatherRepository: FutureWeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(futureWeatherRepository, unitProvider) {

    val weather by lazyDeferred {
        futureWeatherRepository.getCurrentWeather(super.isMetricUnit)
    }
}
