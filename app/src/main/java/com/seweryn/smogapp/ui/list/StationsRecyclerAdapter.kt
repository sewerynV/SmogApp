package com.seweryn.smogapp.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seweryn.smogapp.R
import com.seweryn.smogapp.ui.extensions.showConditionally
import com.seweryn.smogapp.viewmodel.StationsListViewModel.StationWrapper
import kotlinx.android.synthetic.main.item_list.view.*

class StationsRecyclerAdapter : RecyclerView.Adapter<StationsRecyclerAdapter.ViewHolder>() {

    private var stations: List<StationWrapper> = mutableListOf()

    fun updateStations(stations: List<StationWrapper>) {
        this.stations = stations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = stations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stations[position], isItemOnLastPosition(position))
    }

    private fun isItemOnLastPosition(position: Int) = position == itemCount - 1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(stationWrapper: StationWrapper, isLastPosition: Boolean) {
            itemView.item_title.text = stationWrapper.station.stationName
            itemView.item_subtitle.text = "${stationWrapper.station.city.name}, ${stationWrapper.station.city.provinceName}"
            itemView.contentDivider.showConditionally(!isLastPosition)
            itemView.setOnClickListener { stationWrapper.selectAction.invoke() }
        }
    }
}