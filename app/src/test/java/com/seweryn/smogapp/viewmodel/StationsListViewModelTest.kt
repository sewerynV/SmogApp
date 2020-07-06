package com.seweryn.smogapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.data.model.City
import com.seweryn.smogapp.data.model.Station
import com.seweryn.smogapp.utils.SchedulerProvider
import com.seweryn.smogapp.utils.WithMockito
import com.seweryn.smogapp.viewmodel.liveDataModels.Error
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class StationsListViewModelTest : WithMockito {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Mock
    lateinit var smogRepository: SmogRepository
    @Mock
    lateinit var schedulerProvider: SchedulerProvider

    @Mock
    lateinit var progressListener: (Boolean) -> Unit

    @Mock
    lateinit var actionListener: (StationsListViewModel.Action?) -> Unit

    @Before
    fun setUp() {
        mockSchedulers()
    }

    @Test
    fun `should get stations on start`() {
        simulateStationsCanBeLoaded()
        StationsListViewModel(smogRepository, schedulerProvider)
        Mockito.verify(smogRepository).getStations()
    }

    @Test
    fun `should display stations when loaded`() {
        simulateStationsCanBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        assertNotNull(systemUnderTest.stations)
        assertEquals(1, systemUnderTest.stations.value?.size)
    }

    @Test
    fun `should display error when loading failed`() {
        simulateStationsCanNotBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        assertNotNull(systemUnderTest.error)
        assertThat(systemUnderTest.error.value, CoreMatchers.instanceOf(Error::class.java))
    }

    @Test
    fun `should reload stations when selected retry option from error message`() {
        simulateStationsCanNotBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(smogRepository, Mockito.times(2)).getStations()
    }

    @Test
    fun `should save station when it was selected`() {
        simulateStationsCanBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        systemUnderTest.stations.value?.first()?.selectAction?.invoke()
        Mockito.verify(smogRepository).rememberStation(any())
    }

    @Test
    fun `should send close action when selected station and is closable`() {
        simulateStationsCanBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
            .apply { setClosable(true) }
        systemUnderTest.action.observeForever {
            actionListener.invoke(it)
        }
        systemUnderTest.stations.value?.first()?.selectAction?.invoke()
        Mockito.verify(actionListener)
            .invoke(Mockito.any(StationsListViewModel.Action.Close::class.java))
    }

    @Test
    fun `should not send close action when selected station and is not closable`() {
        simulateStationsCanBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
            .apply { setClosable(false) }
        systemUnderTest.action.observeForever {
            actionListener.invoke(it)
        }
        systemUnderTest.stations.value?.first()?.selectAction?.invoke()
        Mockito.verify(actionListener, never())
            .invoke(Mockito.any(StationsListViewModel.Action.Close::class.java))
    }

    @Test
    fun `should show progress when trying to load stations`() {
        simulateStationsCanNotBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        systemUnderTest.progress.observeForever {
            progressListener.invoke(it)
        }
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(progressListener).invoke(true)
    }

    @Test
    fun `should hide progress when stations loaded`() {
        simulateStationsCanNotBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        systemUnderTest.progress.observeForever {
            progressListener.invoke(it)
        }
        simulateStationsCanBeLoaded()
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(progressListener).invoke(true)
        Mockito.verify(progressListener, Mockito.times(2)).invoke(false)
    }

    @Test
    fun `should hide progress on loading error`() {
        simulateStationsCanNotBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        systemUnderTest.progress.observeForever {
            progressListener.invoke(it)
        }
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(progressListener).invoke(true)
        Mockito.verify(progressListener, Mockito.times(2)).invoke(false)
    }


    private fun mockSchedulers() {
        Mockito.`when`(schedulerProvider.ioScheduler()).thenReturn(Schedulers.trampoline())
        Mockito.`when`(schedulerProvider.uiScheduler()).thenReturn(Schedulers.trampoline())
    }

    private fun simulateStationsCanBeLoaded() {
        Mockito.`when`(smogRepository.getStations())
            .thenReturn(Single.just(listOf(createStation())))
    }

    private fun simulateStationsCanNotBeLoaded() {
        Mockito.`when`(smogRepository.getStations()).thenReturn(Single.error(Exception()))
    }

    private fun createStation(): Station {
        return Station(
            id = 1,
            stationName = "name",
            city = City(
                name = "city",
                provinceName = "province"
            )
        )
    }

    @Test
    fun `test map rx`() {
        simulateStationsCanNotBeLoaded()
        val systemUnderTest = StationsListViewModel(smogRepository, schedulerProvider)
        systemUnderTest.testRx()
    }

}