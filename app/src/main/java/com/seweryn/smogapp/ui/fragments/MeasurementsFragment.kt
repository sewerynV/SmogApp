package com.seweryn.smogapp.ui.fragments

import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.seweryn.smogapp.R
import com.seweryn.smogapp.data.model.Measurement
import com.seweryn.smogapp.ui.extensions.fadeAndZoomIn
import com.seweryn.smogapp.ui.extensions.hide
import com.seweryn.smogapp.ui.extensions.show
import com.seweryn.smogapp.ui.extensions.showConditionally
import com.seweryn.smogapp.ui.list.MeasurementsRecyclerAdapter
import com.seweryn.smogapp.ui.utils.IndexMeasurementResourcesProvider
import com.seweryn.smogapp.utils.toStringTrimmingDecimalPlaces
import com.seweryn.smogapp.viewmodel.MeasurementsViewModel
import kotlinx.android.synthetic.main.fragment_measurements.*
import kotlinx.android.synthetic.main.fragment_measurements.view.*

class MeasurementsFragment : BaseFragment<MeasurementsViewModel>() {

    private val adapter = MeasurementsRecyclerAdapter()
    private var eventsListener: EventsListener? = null

    fun eventsListener(eventsListener: EventsListener) {
        this.eventsListener = eventsListener
    }

    override fun viewModel() = ViewModelProvider(this, viewModelFactory).get(MeasurementsViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_measurements, container, false)
        view.measurementsList.layoutManager = LinearLayoutManager(context)
        view.measurementsList.adapter = adapter
        view.changeStationBtn.setOnClickListener { eventsListener?.changeStationSelected() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStation()
        observeMeasurements()
        observeSelectedMeasurement()
        observeError()
        observeProgress()
    }

    private fun observeStation() {
        viewModel.station.observe(viewLifecycleOwner, Observer {
            measurementsStation.text = it.stationName
        })
    }

    private fun observeSelectedMeasurement() {
        viewModel.selectedMeasurement.observe(viewLifecycleOwner, Observer {
            progressWheel.max = it.measurement.maxValue.toInt()
            measurementIndexText.text = resources.getString(IndexMeasurementResourcesProvider.getIndexText(it.measurement.index))
            measurementName.text = it.name
            context?.let { context ->
                val indexColor = ContextCompat.getColor(context, IndexMeasurementResourcesProvider.getIndexColor(it.measurement.index))
                measurementValue.setTextColor(indexColor)
                progressWheel.progressDrawable.setColorFilter(indexColor, PorterDuff.Mode.SRC_IN)
                measurementIndexText.setTextColor(indexColor)
            }
            animateMeasurement(it.measurement)
        })
    }

    private fun observeMeasurements() {
        viewModel.measurements.observe(viewLifecycleOwner, Observer {
            adapter.updateMeasurements(it)
        })
    }

    private fun observeProgress() {
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            listProgress.showConditionally(it)
        })
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, Observer {error ->
            if (error == null) measurements_error_view.hide()
            else {
                measurements_error_view.setError(error)
                measurements_error_view.show()
            }
        })
    }

    private fun animateMeasurement(measurement: Measurement) {
        ValueAnimator.ofFloat(0f, measurement.value).apply {
            duration = 1000
            addUpdateListener {
                progressWheel?.progress = (it.animatedValue as Float).toInt()
                measurementValue?.text = (it.animatedValue as Float).toStringTrimmingDecimalPlaces()
            }
        }.start()
        measurementIndexText.fadeAndZoomIn()

    }

    interface EventsListener {
        fun changeStationSelected()
    }

}