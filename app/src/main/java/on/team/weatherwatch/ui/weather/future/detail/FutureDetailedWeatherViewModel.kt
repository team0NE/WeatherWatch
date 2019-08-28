package on.team.weatherwatch.ui.weather.future.detail

import on.team.weatherwatch.data.provider.UnitProvider
import on.team.weatherwatch.data.repository.FutureWeatherRepository
import on.team.weatherwatch.ui.base.WeatherViewModel
import on.team.weatherwatch.utils.lazyDeferred
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
