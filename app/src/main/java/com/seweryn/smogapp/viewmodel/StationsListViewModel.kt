package com.seweryn.smogapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.data.model.Station
import com.seweryn.smogapp.utils.SchedulerProvider
import com.seweryn.smogapp.viewmodel.liveDataModels.Error
import com.seweryn.smogapp.viewmodel.liveDataModels.ErrorAction.*

class StationsListViewModel(private val smogRepository: SmogRepository,
                            schedulerProvider: SchedulerProvider): BaseViewModel(schedulerProvider) {
    val stations = MutableLiveData<List<StationWrapper>>()
    val progress = MutableLiveData<Boolean>()
    val error = MutableLiveData<Error>()
    var action: MutableLiveData<Action?> = MutableLiveData()

    private var isClosable: Boolean = false

    init {
        getStations()
    }

    fun setClosable(isClosable: Boolean){
        this.isClosable = isClosable
    }

    private fun getStations() {
        progress.value = true
        error.value = null
        load(
            command = smogRepository.getStations(),
            onSuccess = { result ->
                progress.value = false
                stations.value = result.map { mapStation(it) }
            },
            onError = {
                progress.value = false
                error.value = handleError(it, Retry{ getStations() })
            }
        )
    }

    private fun mapStation(station: Station) = StationWrapper(
        station = station,
        selectAction = {
            smogRepository.rememberStation(station)
            if(isClosable) sendSingleEvent(action, Action.Close)
        }
    )

    data class StationWrapper(
        val station: Station,
        val selectAction: () -> Unit)

    sealed class Action{
        object Close: Action()
    }

}