package com.seweryn.smogapp.viewmodel.liveDataModels

sealed class Error(val errorAction: ErrorAction?) {
    class GenericError(errorAction: ErrorAction? = null) : Error(errorAction)
    class ConnectionError(errorAction: ErrorAction? = null) : Error(errorAction)
}