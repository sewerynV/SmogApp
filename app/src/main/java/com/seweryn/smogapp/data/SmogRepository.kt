package com.seweryn.smogapp.data

import com.seweryn.smogapp.data.model.Measurement
import com.seweryn.smogapp.data.model.MeasurementInfo
import com.seweryn.smogapp.data.model.Station
import io.reactivex.Observable
import io.reactivex.Single

interface SmogRepository {
    fun getStations(): Single<List<Station>>

    fun getMeasurements(): Single<List<MeasurementInfo>>

    fun rememberStation(station: Station)

    fun getStation(): Observable<Station>

    fun isStationRemembered(): Observable<Boolean>
}