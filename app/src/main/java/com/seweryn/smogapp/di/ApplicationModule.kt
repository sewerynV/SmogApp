package com.seweryn.smogapp.di

import android.app.Application
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.data.SmogRepositoryImpl
import com.seweryn.smogapp.data.local.sharedprefs.station.StationPreferences
import com.seweryn.smogapp.data.local.sharedprefs.station.StationPreferencesImpl
import com.seweryn.smogapp.data.remote.SmogApi
import com.seweryn.smogapp.utils.SchedulerProvider
import com.seweryn.smogapp.utils.SchedulerProviderImpl
import com.seweryn.smogapp.utils.network.ConnectionManager
import com.seweryn.smogapp.utils.network.ConnectionManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideConnectionManager(): ConnectionManager = ConnectionManagerImpl(application)

    @Provides
    @Singleton
    fun provideSchedulersProvider(): SchedulerProvider = SchedulerProviderImpl()

    @Provides
    @Singleton
    fun provideStationPreferences(): StationPreferences = StationPreferencesImpl(application)

    @Provides
    @Singleton
    fun provideSmogRepository(smogApi: SmogApi, preferences: StationPreferences): SmogRepository =
        SmogRepositoryImpl(smogApi, preferences)
}