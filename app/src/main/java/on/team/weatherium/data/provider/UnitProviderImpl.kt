package on.team.weatherium.data.provider

import android.content.Context
import on.team.weatherium.utils.UNIT_SYSTEM
import on.team.weatherium.utils.UnitSystem

class UnitProviderImpl(context: Context) : PreferencesProvider(context), UnitProvider {

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}