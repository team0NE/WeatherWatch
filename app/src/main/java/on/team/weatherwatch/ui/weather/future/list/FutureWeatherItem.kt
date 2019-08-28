package on.team.weatherwatch.ui.weather.future.list

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.future_list_item.*
import on.team.weatherwatch.R
import on.team.weatherwatch.data.db.unitlocalized.future.list.MetricSimpleFutureWeatherEntry
import on.team.weatherwatch.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import on.team.weatherwatch.utils.glide.GlideApp
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem(
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
) : Item() {

    override fun getLayout() = R.layout.future_list_item
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            item_textView_condition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateIcon()
        }
    }

    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        item_textView_date.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation =
            if (weatherEntry is MetricSimpleFutureWeatherEntry) "°C" else "°F"
        val strHolder = "${weatherEntry.avgTemperature}$unitAbbreviation"
        item_textView_temperature.text = strHolder
    }

    private fun ViewHolder.updateIcon() {
        GlideApp.with(this.containerView)
            .load("http:" + weatherEntry.conditionIconUrl)
            .into(item_imageView_condition_icon)
    }
}