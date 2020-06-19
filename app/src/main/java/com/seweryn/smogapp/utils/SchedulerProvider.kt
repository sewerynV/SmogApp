package com.seweryn.smogapp.utils

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun ioScheduler(): Scheduler

    fun uiScheduler(): Scheduler
}