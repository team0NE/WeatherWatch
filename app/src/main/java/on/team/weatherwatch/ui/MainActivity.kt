package on.team.weatherwatch.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import kotlinx.android.synthetic.main.activity_main.*
import on.team.weatherwatch.R
import on.team.weatherwatch.utils.MY_PERMISSION_ACCESS_COARSE_LOCATION
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()
    private lateinit var navController: NavController

    private val fusedLocationProviderClient: FusedLocationProviderClient by instance()
    private val locationCallback = object : LocationCallback() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        bottom_navi.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        requestLocationPermission()
        if (hasPermission()) {
            bindLocationManager()
        } else requestLocationPermission()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                bindLocationManager()
            else Toast.makeText(this, "Please, set weatherLocation manually in settings", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindLocationManager() {
        LifecycleBoundLocationManager(
            this,
            fusedLocationProviderClient, locationCallback
        )
    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
