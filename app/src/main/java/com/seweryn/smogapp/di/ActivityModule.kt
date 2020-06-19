package com.seweryn.smogapp.di

import com.seweryn.smogapp.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule  {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}