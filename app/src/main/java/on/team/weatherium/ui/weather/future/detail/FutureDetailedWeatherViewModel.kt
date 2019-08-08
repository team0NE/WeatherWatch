package on.team.weatherium.ui.weather.future.detail

import on.team.weatherium.data.provider.UnitProvider
import on.team.weatherium.data.repository.FutureWeatherRepository
import on.team.weatherium.ui.base.WeatherViewModel
import on.team.weatherium.utils.lazyDeferred
import org.threeten.bp.LocalDate

class FutureDetailedWeatherViewModel(
    private val detailedDate: LocalDate,
    private val futureWeatherRepository: FutureWeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(futureWeatherRepository, unitProvider) {
    val detailedWeatherEntry by lazyDeferred {
        futureWeatherRepository.getFutureWeatherByDate(detailedDate, super.isMetricUnit)
    }
}
