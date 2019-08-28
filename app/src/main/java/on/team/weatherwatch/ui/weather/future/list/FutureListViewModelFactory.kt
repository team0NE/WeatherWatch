package on.team.weatherwatch.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import on.team.weatherwatch.data.provider.UnitProvider
import on.team.weatherwatch.data.repository.FutureWeatherRepository

class FutureListViewModelFactory(
    private val futureWeatherRepository: FutureWeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListWeatherViewModel(futureWeatherRepository, unitProvider) as T
    }
}