package com.app.randomuser.ui.weatherAppFragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.randomuser.BuildConfig
import com.app.randomuser.GenericCallback
import com.app.randomuser.R
import com.app.randomuser.databinding.FragmentWeatherBaseBinding
import com.app.randomuser.services.Resource
import com.app.randomuser.ui.weatherAppFragments.viewModels.WeatherViewModel
import com.app.randomuser.utils.LocalUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class WeatherBaseFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fragmentBinding: FragmentWeatherBaseBinding
    private lateinit var fusedLocator: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weather_base, container, false)
        return fragmentBinding.root

    }


    override fun onStart() {
        super.onStart()
        fetchLocation()

    }

    private fun fetchLocation() {
        if (checkLocationPermission() && checkLocationTurnedOn()) {
            showProgressBar()
            val cancellationTokenSource = CancellationTokenSource()
            activity?.let { activity ->
                fusedLocator = LocationServices.getFusedLocationProviderClient(activity)
                fusedLocator.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnCompleteListener { locationTask ->
                    if (locationTask.isComplete && locationTask.isSuccessful) {
                        val location = locationTask.result
                        fetchData(location.latitude, location.longitude)

                    }
                }
            }
        }
    }

    private fun checkLocationTurnedOn(): Boolean {
        activity?.let {
            val activityContext = it
            val locationManager: LocationManager =
                activityContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                true
            } else {
                LocalUtils.showAlertDialog(
                    activityContext,
                    resources.getString(R.string.location_access_title),
                    resources.getString(R.string.location_access_message),
                    object : GenericCallback<String> {
                        override fun callback(data: String) {
                            if (data == "positive") {
                                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }
                        }
                    }
                )
                false
            }
        }
        return false
    }

    val content =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    // Precise location access granted.
                    fetchLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    // Only approximate location access granted.
                    fetchLocation()
                }
                else -> {

                    val permissionOne =
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    val permissionTwo =
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)

                    // No location access granted.
                    activity?.let {
                        LocalUtils.showAlertDialog(it,
                            resources.getString(R.string.location_permission_title),
                            resources.getString(R.string.location_permission_message),
                            object : GenericCallback<String> {
                                override fun callback(data: String) {
                                    if (data == "positive") {
                                        if (permissionOne || permissionTwo)
                                            checkLocationPermission()
                                        else
                                            startActivity(
                                                Intent(
                                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                                                )
                                            )
                                    }
                                }
                            }
                        )
                    }


                }
            }
        }

    private fun checkLocationPermission(): Boolean {
        activity?.let { context ->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                content.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )

                return false
            } else {
                return true
            }
        }
        return false
    }


    private fun fetchData(latitude: Double, longitude: Double) {
        activity?.let {
            if (LocalUtils.isOnline(it)) {
                weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
                weatherViewModel.getWeatherReport(latitude, longitude)
                    .observe(viewLifecycleOwner, {
                        run {
                            val weatherData = it
                            when (weatherData?.status) {

                                Resource.Status.SUCCESS -> {
                                    hideProgressBar()
                                    fragmentBinding.weather = weatherData.data
                                }
                                Resource.Status.LOADING -> {
                                    showProgressBar()
                                }
                                Resource.Status.ERROR -> {
                                    hideProgressBar()
                                }
                            }
                        }
                    })
            } else {
                LocalUtils.showAlertDialog(it,
                    resources.getString(R.string.connection_error),
                    resources.getString(R.string.internet_connection_error),
                    object : GenericCallback<String> {
                        override fun callback(data: String) {
                            if (data == "positive") {
                                fetchData(latitude, longitude)
                            }
                        }
                    })
            }
        }

    }

    private fun showProgressBar() {
        fragmentBinding.progressbarLayout.visibility = View.VISIBLE

    }

    private fun hideProgressBar() {
        fragmentBinding.progressbarLayout.visibility = View.GONE

    }

}