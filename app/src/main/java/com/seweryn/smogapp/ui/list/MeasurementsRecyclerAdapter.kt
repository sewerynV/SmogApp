package com.seweryn.smogapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seweryn.smogapp.R
import com.seweryn.smogapp.ui.extensions.showConditionally
import com.seweryn.smogapp.viewmodel.MeasurementsViewModel.MeasurementInfoWrapper
import kotlinx.android.synthetic.main.item_list.view.*

class MeasurementsRecyclerAdapter : RecyclerView.Adapter<MeasurementsRecyclerAdapter.ViewHolder>() {

    private var measurements: List<MeasurementInfoWrapper> = mutableListOf()

    fun updateMeasurements(measurements: List<MeasurementInfoWrapper>) {
        this.measurements = measurements
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = measurements.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(measurements[position], isItemOnLastPosition(position))
    }

    private fun isItemOnLastPosition(position: Int) = position == itemCount - 1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(measurementWrapper: MeasurementInfoWrapper, isLastPosition: Boolean) {
            itemView.item_title.text = measurementWrapper.measurementInfo.name
            itemView.item_subtitle.text = measurementWrapper.measurementInfo.code
            itemView.contentDivider.showConditionally(!isLastPosition)
            itemView.setOnClickListener { measurementWrapper.selectAction.invoke() }
        }
    }
}