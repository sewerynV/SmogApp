package com.seweryn.smogapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: SmogRepository

    @Mock
    lateinit var schedulerProvider: SchedulerProvider

    @Before
    fun setUp() {
        mockSchedulers()
    }

    @Test
    fun `should set state to list when station is not remembered`() {
        simulateThatStationIsNotRemembered()
        val systemUnderTest = MainViewModel(repository, schedulerProvider)
        assertNotNull(systemUnderTest.state)
        assertEquals(MainViewModel.State.LIST, systemUnderTest.state.value)
    }

    @Test
    fun `should set state to details when station is remembered`() {
        simulateThatStationIsRemembered()
        val systemUnderTest = MainViewModel(repository, schedulerProvider)
        assertNotNull(systemUnderTest.state)
        assertEquals(MainViewModel.State.DETAIL, systemUnderTest.state.value)
    }

    private fun simulateThatStationIsRemembered() {
        Mockito.`when`(repository.isStationRemembered()).thenReturn(Observable.just(true))
    }

    private fun simulateThatStationIsNotRemembered() {
        Mockito.`when`(repository.isStationRemembered()).thenReturn(Observable.just(false))
    }

    private fun mockSchedulers() {
        Mockito.`when`(schedulerProvider.ioScheduler()).thenReturn(Schedulers.trampoline())
        Mockito.`when`(schedulerProvider.uiScheduler()).thenReturn(Schedulers.trampoline())
    }
}