package on.team.weatherium.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import on.team.weatherium.data.provider.UnitProvider
import on.team.weatherium.data.repository.FutureWeatherRepository

class FutureListViewModelFactory(
    private val futureWeatherRepository: FutureWeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListWeatherViewModel(futureWeatherRepository, unitProvider) as T
    }
}