package com.seweryn.smogapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.utils.SchedulerProvider
import com.seweryn.smogapp.viewmodel.MainViewModel
import com.seweryn.smogapp.viewmodel.MeasurementsViewModel
import com.seweryn.smogapp.viewmodel.StationsListViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
class ViewModelModule  {

    @MapKey
    @Target(AnnotationTarget.FUNCTION)
    annotation class ViewModelKey(
        val value: KClass<out ViewModel>
    )

    @Provides
    @IntoMap
    @ViewModelKey(StationsListViewModel::class)
    fun provideStationsListViewModel(repository: SmogRepository, schedulerProvider: SchedulerProvider): ViewModel {
        return StationsListViewModel(repository, schedulerProvider)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(repository: SmogRepository, schedulerProvider: SchedulerProvider): ViewModel {
        return MainViewModel(repository, schedulerProvider)
    }

    @Provides
    @IntoMap
    @ViewModelKey(MeasurementsViewModel::class)
    fun provideMeasurementsViewModel(repository: SmogRepository, schedulerProvider: SchedulerProvider): ViewModel {
        return MeasurementsViewModel(repository , schedulerProvider)
    }

    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return requireNotNull(providers[modelClass as Class<out ViewModel>]).get() as T
        }
    }

}