package com.seweryn.smogapp.di

import com.seweryn.smogapp.ui.fragments.MeasurementsFragment
import com.seweryn.smogapp.ui.fragments.StationsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeStationsListFragment(): StationsListFragment

    @ContributesAndroidInjector
    abstract fun contributeMeasurementsFragment(): MeasurementsFragment
}