package com.seweryn.smogapp.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seweryn.smogapp.R
import com.seweryn.smogapp.ui.fragments.MeasurementsFragment
import com.seweryn.smogapp.ui.fragments.StationsListFragment
import com.seweryn.smogapp.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel>(), MeasurementsFragment.EventsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.state.observe(this, Observer { state ->
            showFragment(
                when (state) {
                    MainViewModel.State.LIST -> {
                        StationsListFragment()
                    }
                    MainViewModel.State.DETAIL -> {
                        MeasurementsFragment().apply { eventsListener(this@MainActivity) }
                    }
                },
                state.name
            )


        })
        if(savedInstanceState != null) {
            listenToMeasurementsFragmentEvents()
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val foundFragment = supportFragmentManager.findFragmentByTag(tag)
        if (foundFragment == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragment, tag)
                commit()
            }
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun listenToMeasurementsFragmentEvents() {
        val fragment = supportFragmentManager.findFragmentByTag(MainViewModel.State.DETAIL.name)
        if(fragment != null) {
            (fragment as MeasurementsFragment).eventsListener(this)
        }
    }


    override fun viewModel() =
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

    override fun changeStationSelected() {
        addFragment(StationsListFragment.newInstance(true))
    }
}

