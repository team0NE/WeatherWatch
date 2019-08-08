package on.team.weatherium.ui.weather.future.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import on.team.weatherium.R
import on.team.weatherium.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeatherEntry
import on.team.weatherium.ui.base.ScopedFragment
import on.team.weatherium.utils.LocalDateConverter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

class FutureListWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: FutureListViewModelFactory by instance()
    private lateinit var viewModel: FutureListWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FutureListWeatherViewModel::class.java)
        buildUI()
    }

    private fun buildUI() = launch(Dispatchers.Main) {
        val futureWeatherEntry = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@FutureListWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })
        futureWeatherEntry.observe(this@FutureListWeatherFragment, Observer { weatherEntries ->
            if (weatherEntries == null) return@Observer

            future_group_loading.visibility = View.GONE
            updateDateToNextWeek()

            initRView(weatherEntries.toFutureItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(R.string.next_7days)
    }

    private fun initRView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FutureWeatherItem)?.let {
                showWeatherDetail(it.weatherEntry.date, view)
            }
        }
    }

    private fun List<UnitSpecificSimpleFutureWeatherEntry>.toFutureItems(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it)
        }
    }

    private fun showWeatherDetail(date: LocalDate, view: View) {
        val dateString = LocalDateConverter.dateToString(date)!!
        val actionDetail = FutureListWeatherFragmentDirections.actionDetail(dateString)
        Navigation.findNavController(view).navigate(actionDetail)
    }
}
