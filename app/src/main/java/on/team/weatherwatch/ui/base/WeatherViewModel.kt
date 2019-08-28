package on.team.weatherwatch.ui.base

import androidx.lifecycle.ViewModel
import on.team.weatherwatch.data.provider.UnitProvider
import on.team.weatherwatch.data.repository.FutureWeatherRepository
import on.team.weatherwatch.utils.UnitSystem
import on.team.weatherwatch.utils.lazyDeferred

abstract class WeatherViewModel(
    private val futureWeatherRepository: FutureWeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()
    val isMetricUnit: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        futureWeatherRepository.getWeatherLocation()
    }
}