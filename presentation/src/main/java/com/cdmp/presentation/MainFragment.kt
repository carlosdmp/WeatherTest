package com.cdmp.presentation

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.cdmp.domain.model.Point
import com.cdmp.domain.model.WeatherRequest
import com.cdmp.presentation.model.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.cdmp.presentation.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var currentDialog: AlertDialog? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: com.cdmp.presentation.databinding.FragmentMainBinding

    private val viewModel: MainViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setupInput(etNameInput, ibNameSearch) { WeatherRequest.CityRequest(it) }
            setupInput(etZipInput, ibZipSearch) { WeatherRequest.ZipRequest(it) }

            viewModel.weatherInfo.observe(viewLifecycleOwner, Observer { info ->
                info.fold(
                    { error ->
                        showError(error)
                    }, { display ->
                        showDisplay(display)
                    })
            })
            viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                pbLoading.isVisible = isLoading
                ibNameSearch.isEnabled = !isLoading
                ibNameSearch.isEnabled = !isLoading
            })
        }
    }

    private fun showDisplay(display: WeatherDisplay) {
        with(binding) {
            display.highestTemperature?.let {
                iMaxTemp.isVisible = true
                iMaxTemp.setValue(
                    getString(
                        R.string.max_temp_value,
                        getPointLabel(it.first),
                        it.second
                    )
                )
            }
            display.highestHumidity?.let {
                iMaxHumidity.isVisible = true
                iMaxHumidity.setValue(
                    getString(
                        R.string.max_humidity_value,
                        getPointLabel(it.first),
                        it.second
                    )
                )
            }
            when (val rain = display.highestRain) {
                null -> iMaxRain.isVisible = false
                else -> {
                    iMaxRain.isVisible = true
                    iMaxRain.setValue(
                        getString(
                            R.string.max_raining_value,
                            getPointLabel(rain.first),
                            rain.second
                        )
                    )
                }
            }
            display.highestWind?.let {
                iMaxSpeed.isVisible = true
                iMaxSpeed.setValue(
                    getString(
                        R.string.max_wind_speed_value,
                        getPointLabel(it.first),
                        it.second
                    )
                )
            }
        }
    }

    private fun showError(error: ErrorDisplay) {
        with(binding) {
            iMaxTemp.isVisible = false
            iMaxSpeed.isVisible = false
            iMaxHumidity.isVisible = false
            iMaxRain.isVisible = false
        }
        context?.let { context ->
            currentDialog?.dismiss()
            currentDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.error_dialog_header))
                .setMessage(
                    when (error) {
                        NotFoundErrorDisplay -> getString(R.string.not_found_error_message)
                        BadRequestErrorDisplay -> getString(R.string.bad_input_error_message)
                        is UnknownErrorDisplay -> error.message
                            ?: getString(R.string.unknown_error_message)
                    }
                )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }

    private fun setupInput(input: EditText, icon: ImageView, requestBuilder: (String) -> WeatherRequest) {
        icon.setOnClickListener {
            input.text?.toString()?.let { query ->
                viewModel.onNewSearch(requestBuilder(query))
            }
            (activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).run {
                hideSoftInputFromWindow(binding.root.windowToken, 0)
            }
        }
    }

    private fun getPointLabel(point: Point) = getString(
        when (point) {
            Point.ORIGIN -> R.string.origin_point_label
            Point.NORTH -> R.string.north_point_label
            Point.SOUTH -> R.string.south_point_label
            Point.EAST -> R.string.east_point_label
            Point.WEST -> R.string.west_point_label
        }
    )
}
