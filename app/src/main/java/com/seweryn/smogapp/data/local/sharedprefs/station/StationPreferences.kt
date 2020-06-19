package com.seweryn.smogapp.data.local.sharedprefs.station

import com.seweryn.smogapp.data.model.Station
import io.reactivex.Observable

interface StationPreferences {
    fun getStationId(): Int
    fun getStation(): Station
    fun rememberStation(station: Station)
    fun observeStation(): Observable<Station>
}