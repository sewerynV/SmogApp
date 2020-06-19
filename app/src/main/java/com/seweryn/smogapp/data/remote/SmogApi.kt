package com.seweryn.smogapp.data.remote

import com.seweryn.smogapp.data.model.MeasurementResponse
import com.seweryn.smogapp.data.model.Sensor
import com.seweryn.smogapp.data.model.Station
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface SmogApi  {
    @GET("station/findAll")
    fun getStations(): Single<List<Station>>

    @GET("station/sensors/{stationId}")
    fun getSensors(@Path("stationId") stationId: Int): Single<List<Sensor>>

    @GET("data/getData/{sensorId}")
    fun getMeasurements(@Path("sensorId") sensorId: Int): Single<MeasurementResponse>
}