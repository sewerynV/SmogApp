package com.seweryn.smogapp.data.model

data class MeasurementResponse(val values: List<MeasurementResponseValue>)

data class MeasurementResponseValue(val date: String, val value: Float?)