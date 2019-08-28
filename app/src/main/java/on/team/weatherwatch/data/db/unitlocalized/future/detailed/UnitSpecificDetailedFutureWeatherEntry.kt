package on.team.weatherwatch.data.db.unitlocalized.future.detailed

import org.threeten.bp.LocalDate

interface UnitSpecificDetailedFutureWeatherEntry {
    val date: LocalDate
    val minTemperature: Double
    val maxTemperature: Double
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val windMaxSpeed: Double
    val totalPrecipitation: Double
    val avgVisibilityDistance: Double
    val uv: Double
}