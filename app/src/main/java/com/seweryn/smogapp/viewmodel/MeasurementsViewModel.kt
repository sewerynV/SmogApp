package com.seweryn.smogapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.data.model.Measurement
import com.seweryn.smogapp.data.model.MeasurementInfo
import com.seweryn.smogapp.data.model.Station
import com.seweryn.smogapp.utils.SchedulerProvider
import com.seweryn.smogapp.viewmodel.liveDataModels.Error
import com.seweryn.smogapp.viewmodel.liveDataModels.ErrorAction.*

class MeasurementsViewModel(private val smogRepository: SmogRepository,
                            schedulerProvider: SchedulerProvider
): BaseViewModel(schedulerProvider) {
    val station = MutableLiveData<Station>()
    val selectedMeasurement = MutableLiveData<MeasurementWrapper>()
    val measurements = MutableLiveData<List<MeasurementInfoWrapper>>()
    val progress = MutableLiveData<Boolean>()
    val error = MutableLiveData<Error>()

    init {
        getStation()
        getMeasurements()
    }

    private fun getStation() {
        load(
            command = smogRepository.getStation(),
            onNext = { result ->
                station.value = result
            },
            onError = {  }
        )
    }

    private fun getMeasurements() {
        progress.value = true
        error.value = null
        load(
            command = smogRepository.getMeasurements(),
            onSuccess = { result ->
                progress.value = false
                measurements.value = result.map { mapMeasurementInfo(it) }
                selectedMeasurement.value = result.first().let {
                    mapMeasurement(it.measurements.first(), it.name)
                }
            },
            onError = {
                progress.value = false
                error.value = handleError(it, Retry{ getMeasurements() })
            }
        )
    }

    private fun mapMeasurementInfo(measurementInfo: MeasurementInfo) = MeasurementInfoWrapper(
        measurementInfo = measurementInfo,
        selectAction = { selectedMeasurement.value = mapMeasurement(measurementInfo.measurements.first(), measurementInfo.name) }
    )

    private fun mapMeasurement(measurement: Measurement, name: String) = MeasurementWrapper(
        measurement = measurement,
        name = name
    )

    data class MeasurementWrapper(
        val measurement: Measurement,
        val name: String
    )

    data class MeasurementInfoWrapper(
        val measurementInfo: MeasurementInfo,
        val selectAction: () -> Unit
    )

}