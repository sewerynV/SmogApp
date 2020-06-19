package com.seweryn.smogapp.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.seweryn.smogapp.R
import com.seweryn.smogapp.ui.extensions.hide
import com.seweryn.smogapp.ui.extensions.show
import com.seweryn.smogapp.ui.extensions.showConditionally
import com.seweryn.smogapp.ui.list.StationsRecyclerAdapter
import com.seweryn.smogapp.viewmodel.StationsListViewModel
import kotlinx.android.synthetic.main.fragment_stations_list.*
import kotlinx.android.synthetic.main.fragment_stations_list.view.*
import com.seweryn.smogapp.viewmodel.StationsListViewModel.Action.Close

class StationsListFragment : BaseFragment<StationsListViewModel>() {

    companion object {
        private val CLOSABLE = "closable"

        fun newInstance(isClosable: Boolean = false): StationsListFragment {
            return StationsListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(CLOSABLE, isClosable)
                }
            }
        }
    }

    private val adapter = StationsRecyclerAdapter()

    override fun viewModel() = ViewModelProvider(this, viewModelFactory).get(StationsListViewModel::class.java).apply {
        arguments?.let {
            setClosable(it.getBoolean(CLOSABLE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stations_list, container, false)
        view.stationsList.layoutManager = LinearLayoutManager(context)
        view.stationsList.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStations()
        observeError()
        observeProgress()
        observeAction()
    }

    private fun observeStations() {
        viewModel.stations.observe(viewLifecycleOwner, Observer {
            adapter.updateStations(it)
        })
    }

    private fun observeProgress() {
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            listProgress.showConditionally(it)
        })
    }

    private fun observeError() {
        viewModel.error.observe(viewLifecycleOwner, Observer {error ->
            if (error == null) stations_error_view.hide()
            else {
                stations_error_view.setError(error)
                stations_error_view.show()
            }
        })
    }

    private fun observeAction() {
        viewModel.action.observe(viewLifecycleOwner, Observer { action ->
            when(action) {
                is Close -> (context as Activity).onBackPressed()
            }
        })
    }

}