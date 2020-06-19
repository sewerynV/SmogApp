package com.seweryn.smogapp.data

import com.seweryn.smogapp.data.error.StationNotSelectedError
import com.seweryn.smogapp.data.local.sharedprefs.station.StationPreferences
import com.seweryn.smogapp.data.model.City
import com.seweryn.smogapp.data.model.MeasurementResponse
import com.seweryn.smogapp.data.model.Station
import com.seweryn.smogapp.data.remote.SmogApi
import com.seweryn.smogapp.utils.WithMockito
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class SmogRepositoryImplTest : WithMockito {

    private lateinit var systemUnderTest: SmogRepositoryImpl

    @Mock
    lateinit var stationPreferences: StationPreferences

    @Mock
    lateinit var api: SmogApi

    @Before
    fun setUp() {
        systemUnderTest = SmogRepositoryImpl(api, stationPreferences)
    }

    @Test
    fun `should call api when stations list requested`() {
        simulateStationsCanBeFetched()
        systemUnderTest.getStations()

        verify(api).getStations()
    }

    @Test
    fun `should throw error when trying to load measurements and station id is not remembered`() {
        simulateStationIdCanNotBeLoadedFromPreferences()
        try {
            systemUnderTest.getMeasurements()
            fail("Should have thrown StationNotSelectedError")
        } catch (e: StationNotSelectedError) {
            //success
        }
    }

    @Test
    fun `should save station to preferences when requested`() {
        systemUnderTest.rememberStation(createStation())

        verify(stationPreferences).rememberStation(any())
    }

    @Test
    fun `should return that station is remembered when saved station is valid`() {
        simulateStationCanBeLoadedFromPreferences()
        val result = systemUnderTest.isStationRemembered().test().values().first()

        assert(result)
    }

    @Test
    fun `should return that station is not remembered when saved station is not valid`() {
        simulateStationCanNotBeLoadedFromPreferences()
        val result = systemUnderTest.isStationRemembered().test().values().first()

        assert(!result)
    }

    private fun simulateStationsCanBeFetched() {
        Mockito.`when`(api.getStations()).thenReturn(Single.just(listOf()))
    }

    private fun simulateStationIdCanNotBeLoadedFromPreferences() {
        Mockito.`when`(stationPreferences.getStationId()).thenReturn(-1)
    }

    private fun simulateStationCanBeLoadedFromPreferences() {
        Mockito.`when`(stationPreferences.observeStation())
            .thenReturn(Observable.just(createStation()))
    }

    private fun simulateStationCanNotBeLoadedFromPreferences() {
        Mockito.`when`(stationPreferences.observeStation())
            .thenReturn(Observable.just(createInvalidStation()))
    }

    private fun createMeasurementResponse() = MeasurementResponse(
        values = listOf()
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

    private fun createInvalidStation(): Station {
        return Station(
            id = -1,
            stationName = "name",
            city = City(
                name = "city",
                provinceName = "province"
            )
        )
    }
}