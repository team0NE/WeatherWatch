package on.team.weatherwatch.data.provider

import on.team.weatherwatch.utils.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}