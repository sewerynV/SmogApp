package com.seweryn.smogapp.data

import com.seweryn.smogapp.data.model.*
import com.seweryn.smogapp.utils.index.*

class MeasurementMapper {
    companion object {
        fun mapMeasurement(measurementResponse: MeasurementResponse, sensor: Sensor): MeasurementInfo {
            val measurementCode = MeasurementCode.values().find { it.code == sensor.param.paramCode}
                ?: throw IllegalArgumentException("Unknown sensor parameter!")
            val indexCalculator = when(measurementCode) {
                MeasurementCode.PM10 -> PM10IndexCalculator()
                MeasurementCode.PM2 -> PM2IndexCalculator()
                MeasurementCode.CO -> COIndexCalculator()
                MeasurementCode.SO2 -> SO2IndexCalculator()
                MeasurementCode.C6H6 -> C6H6IndexCalculator()
                MeasurementCode.O3 -> O3IndexCalculator()
                MeasurementCode.NO2 -> NO2IndexCalculator()
            }
            return MeasurementInfo(
                name = sensor.param.paramName,
                code = sensor.param.paramCode,
                measurements = measurementResponse.values.mapNotNull {
                    if(it.value == null) null
                    else Measurement(
                        date = it.date,
                        value = it.value,
                        index = indexCalculator.calculateIndex(it.value),
                        maxValue = indexCalculator.maxValue()
                    )
                }
            )
        }
    }
}