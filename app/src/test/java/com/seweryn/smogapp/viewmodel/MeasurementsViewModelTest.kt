package com.seweryn.smogapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.data.model.*
import com.seweryn.smogapp.utils.SchedulerProvider
import com.seweryn.smogapp.utils.WithMockito
import com.seweryn.smogapp.viewmodel.liveDataModels.Error
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class MeasurementsViewModelTest : WithMockito {

    private lateinit var systemUnderTest: MeasurementsViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Mock
    lateinit var smogRepository: SmogRepository
    @Mock
    lateinit var schedulerProvider: SchedulerProvider

    @Mock
    lateinit var progressListener: (Boolean) -> Unit

    @Before
    fun setUp() {
        mockSchedulers()
        simulateStationCanBeLoaded()
    }

    @Test
    fun `should load measurements on initialization`() {
        simulateMeasurementsCanBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)

        Mockito.verify(smogRepository).getMeasurements()
    }

    @Test
    fun `should set measurements when it was loaded`() {
        simulateMeasurementsCanBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)


        assertNotNull(systemUnderTest.measurements)
        assertEquals(1, systemUnderTest.measurements.value?.size)
    }

    @Test
    fun `should set selected measurements when it was loaded`() {
        simulateMeasurementsCanBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)


        assertNotNull(systemUnderTest.selectedMeasurement)
        assertEquals("name", systemUnderTest.selectedMeasurement.value?.name)
    }

    @Test
    fun `should set first measurement as selected when it was loaded`() {
        simulateMeasurementsCanBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)


        assertNotNull(systemUnderTest.selectedMeasurement)
        assertEquals("date1", systemUnderTest.selectedMeasurement.value?.measurement?.date)
    }

    @Test
    fun `should set station when it was loaded`() {
        simulateMeasurementsCanBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)


        assertNotNull(systemUnderTest.station)
        assertEquals(1, systemUnderTest.station.value?.id)
    }

    @Test
    fun `should display error when loading failed`() {
        simulateMeasurementsCanNotBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)
        assertNotNull(systemUnderTest.error)
        assertThat(systemUnderTest.error.value, CoreMatchers.instanceOf(Error::class.java))
    }

    @Test
    fun `should reload stations when selected retry option from error message`() {
        simulateMeasurementsCanNotBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(smogRepository, Mockito.times(2)).getMeasurements()
    }

    @Test
    fun `should show progress when trying to load  measurements`() {
        simulateMeasurementsCanNotBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)
        systemUnderTest.progress.observeForever {
            progressListener.invoke(it)
        }
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(progressListener).invoke(true)
    }

    @Test
    fun `should hide progress when measurements loaded`() {
        simulateMeasurementsCanNotBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)
        systemUnderTest.progress.observeForever {
            progressListener.invoke(it)
        }
        simulateMeasurementsCanBeLoaded()
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(progressListener).invoke(true)
        Mockito.verify(progressListener, Mockito.times(2)).invoke(false)
    }

    @Test
    fun `should hide progress on loading error`() {
        simulateMeasurementsCanNotBeLoaded()
        systemUnderTest = MeasurementsViewModel(smogRepository, schedulerProvider)
        systemUnderTest.progress.observeForever {
            progressListener.invoke(it)
        }
        systemUnderTest.error.value?.errorAction?.action?.invoke()
        Mockito.verify(progressListener).invoke(true)
        Mockito.verify(progressListener, Mockito.times(2)).invoke(false)
    }

    private fun simulateMeasurementsCanBeLoaded() {
        Mockito.`when`(smogRepository.getMeasurements())
            .thenReturn(Single.just(listOf(createMeasurementInfo())))
    }

    private fun simulateMeasurementsCanNotBeLoaded() {
        Mockito.`when`(smogRepository.getMeasurements())
            .thenReturn(Single.error(Exception()))
    }

    private fun simulateStationCanBeLoaded() {
        Mockito.`when`(smogRepository.getStation())
            .thenReturn(Observable.just(createStation()))
    }

    private fun mockSchedulers() {
        Mockito.`when`(schedulerProvider.ioScheduler()).thenReturn(Schedulers.trampoline())
        Mockito.`when`(schedulerProvider.uiScheduler()).thenReturn(Schedulers.trampoline())
    }

    private fun createMeasurementInfo() = MeasurementInfo(
        name = "name",
        code = "code",
        measurements = listOf(
            Measurement(
                date = "date1",
                value = 1f,
                maxValue = 2f,
                index = MeasurementIndex.NO_INDEX
            ),
            Measurement(
                date = "date2",
                value = 1f,
                maxValue = 2f,
                index = MeasurementIndex.NO_INDEX
            )
        )
    )

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
}