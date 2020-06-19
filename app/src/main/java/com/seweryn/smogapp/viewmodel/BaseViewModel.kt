package com.seweryn.smogapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seweryn.smogapp.tools.network.error.ConnectionError
import com.seweryn.smogapp.utils.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import com.seweryn.smogapp.viewmodel.liveDataModels.Error
import com.seweryn.smogapp.viewmodel.liveDataModels.ErrorAction
import io.reactivex.Observable

abstract class BaseViewModel(private val schedulersProvider: SchedulerProvider) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }

    protected fun <T> load(
        command: Single<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        subscriptions.add(
            command.subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe(
                    { result -> onSuccess.invoke(result) },
                    { error -> onError.invoke(error) })
        )
    }

    protected fun <T> load(
        command: Observable<T>,
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit = {}
    ) {
        subscriptions.add(
            command.subscribeOn(schedulersProvider.ioScheduler())
                .observeOn(schedulersProvider.uiScheduler())
                .subscribe(
                    { result -> onNext.invoke(result) },
                    { error -> onError.invoke(error) },
                    { onComplete.invoke() })
        )
    }

    protected fun <T> sendSingleEvent(liveData: MutableLiveData<T?>, value: T) {
        liveData.value = value
        liveData.value = null
    }

    protected fun handleError(throwable: Throwable, action: ErrorAction? = null): Error{
        return when(throwable) {
            is ConnectionError -> Error.ConnectionError(action)
            else -> Error.GenericError(action)
        }
    }
}