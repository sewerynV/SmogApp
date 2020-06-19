package com.seweryn.smogapp.data

import com.seweryn.smogapp.data.error.StationNotSelectedError
import com.seweryn.smogapp.data.local.sharedprefs.station.StationPreferences
import com.seweryn.smogapp.data.model.Measurement
import com.seweryn.smogapp.data.model.MeasurementCode
import com.seweryn.smogapp.data.model.MeasurementInfo
import com.seweryn.smogapp.data.model.Station
import com.seweryn.smogapp.data.remote.SmogApi
import io.reactivex.Observable
import io.reactivex.Single

class SmogRepositoryImpl(
    private val api: SmogApi,
    private val stationPreferences: StationPreferences
) : SmogRepository {
    override fun getStations(): Single<List<Station>> {
        return api.getStations()
    }

    override fun getMeasurements(): Single<List<MeasurementInfo>> {
        val stationId = stationPreferences.getStationId()
        if (stationId <= 0) throw StationNotSelectedError()
        return api.getSensors(stationId)
            .flattenAsObservable { it }
            .flatMap { sensor ->
                api.getMeasurements(sensor.id)
                    .map { MeasurementMapper.mapMeasurement(it, sensor) }.toObservable()
            }.toList()
    }

    override fun rememberStation(station: Station) = stationPreferences.rememberStation(station)

    override fun getStation() = stationPreferences.observeStation()

    override fun isStationRemembered(): Observable<Boolean> {
        return stationPreferences.observeStation()
            .map { it.id > 0 }
    }

}