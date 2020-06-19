package com.seweryn.smogapp.utils.index

import com.seweryn.smogapp.data.model.MeasurementIndex
import com.seweryn.smogapp.data.model.MeasurementIndex.*


abstract class IndexCalculator {

    protected abstract val veryGoodMaxValue: Float
    protected abstract val goodMaxValue: Float
    protected abstract val moderateMaxValue: Float
    protected abstract val sufficientMaxValue: Float
    protected abstract val badMaxValue: Float

    fun calculateIndex(value: Float): MeasurementIndex {
        return when {
            value < 0 -> NO_INDEX
            value <= veryGoodMaxValue -> VERY_GOOD
            value <= goodMaxValue -> GOOD
            value <= moderateMaxValue -> MODERATE
            value <= sufficientMaxValue -> SUFFICIENT
            value <= badMaxValue -> BAD
            else -> VERY_BAD
        }
    }

    fun maxValue() = badMaxValue

}