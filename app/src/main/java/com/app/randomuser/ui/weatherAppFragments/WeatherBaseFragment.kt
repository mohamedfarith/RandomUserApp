package com.app.randomuser.ui.weatherAppFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.randomuser.R
import com.app.randomuser.databinding.FragmentWeatherBaseBinding
import com.app.randomuser.services.Resource
import com.app.randomuser.ui.weatherAppFragments.viewModels.WeatherViewModel

class WeatherBaseFragment : Fragment() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fragmentBinding: FragmentWeatherBaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weather_base, container, false)
        return fragmentBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()

    }

    private fun fetchData() {
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        weatherViewModel.getWeatherReport(23.7104, 90.4074)
            .observe(viewLifecycleOwner, {
                run {
                    val weatherData = it
                    when (weatherData?.status) {

                        Resource.Status.SUCCESS -> {
                            fragmentBinding.weather = weatherData.data
                        }
                        Resource.Status.LOADING -> {

                        }
                        Resource.Status.ERROR -> {

                        }
                    }
                }
            })
    }

}