package on.team.weatherium.data.provider

import on.team.weatherium.utils.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}