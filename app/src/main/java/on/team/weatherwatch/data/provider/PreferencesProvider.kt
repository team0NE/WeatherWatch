package on.team.weatherwatch.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferencesProvider(context: Context) {
    private var appContext = context.applicationContext
    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}