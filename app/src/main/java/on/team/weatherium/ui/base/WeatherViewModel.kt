package on.team.weatherium.ui.base

import androidx.lifecycle.ViewModel
import on.team.weatherium.data.provider.UnitProvider
import on.team.weatherium.data.repository.FutureWeatherRepository
import on.team.weatherium.utils.UnitSystem
import on.team.weatherium.utils.lazyDeferred

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