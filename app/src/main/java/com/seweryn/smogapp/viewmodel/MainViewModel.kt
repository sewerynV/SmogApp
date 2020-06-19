package com.seweryn.smogapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.seweryn.smogapp.data.SmogRepository
import com.seweryn.smogapp.utils.SchedulerProvider

class MainViewModel(repository: SmogRepository,
                    schedulerProvider: SchedulerProvider
) : BaseViewModel(schedulerProvider) {

    val state = MutableLiveData<State>()

    init {
        load(
            command = repository.isStationRemembered(),
            onNext = { isStationRemembered ->
                state.value =
                    if(isStationRemembered) State.DETAIL else State.LIST
            },
            onError = { state.value = State.LIST }
        )

    }

    enum class State{
        LIST, DETAIL
    }
}