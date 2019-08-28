package on.team.weatherwatch.data.db.unitlocalized.future.detailed

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

data class MetricDetailedFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempC")
    override val avgTemperature: Double,
    @ColumnInfo(name = "mintempC")
    override val minTemperature: Double,
    @ColumnInfo(name = "maxtempC")
    override val maxTemperature: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "maxwindKph")
    override val windMaxSpeed: Double,
    @ColumnInfo(name = "totalprecipMm")
    override val totalPrecipitation: Double,
    @ColumnInfo(name = "avgvisKm")
    override val avgVisibilityDistance: Double,
    @ColumnInfo(name = "uv")
    override val uv: Double
) : UnitSpecificDetailedFutureWeatherEntry