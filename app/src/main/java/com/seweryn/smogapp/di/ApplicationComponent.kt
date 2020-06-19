package com.seweryn.smogapp.di

import com.seweryn.smogapp.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ApplicationModule::class, NetworkModule::class, ActivityModule::class, FragmentModule::class, ViewModelModule::class])
interface ApplicationComponent : AndroidInjector<App>