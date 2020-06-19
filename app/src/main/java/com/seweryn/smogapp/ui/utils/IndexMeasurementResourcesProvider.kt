package com.seweryn.smogapp.ui.utils

import com.seweryn.smogapp.R
import com.seweryn.smogapp.data.model.MeasurementIndex

class IndexMeasurementResourcesProvider {
    companion object {
        fun getIndexColor(index: MeasurementIndex): Int {
            return when(index) {
                MeasurementIndex.VERY_GOOD -> R.color.index_very_good
                MeasurementIndex.GOOD -> R.color.index_good
                MeasurementIndex.MODERATE -> R.color.index_moderate
                MeasurementIndex.SUFFICIENT -> R.color.index_sufficient
                MeasurementIndex.BAD -> R.color.index_bad
                MeasurementIndex.VERY_BAD -> R.color.index_very_bad
                MeasurementIndex.NO_INDEX -> R.color.no_index
            }
        }

        fun getIndexText(index: MeasurementIndex): Int {
            return when(index) {
                MeasurementIndex.VERY_GOOD -> R.string.index_very_good
                MeasurementIndex.GOOD -> R.string.index_good
                MeasurementIndex.MODERATE -> R.string.index_moderate
                MeasurementIndex.SUFFICIENT -> R.string.index_sufficient
                MeasurementIndex.BAD -> R.string.index_bad
                MeasurementIndex.VERY_BAD -> R.string.index_very_bad
                MeasurementIndex.NO_INDEX -> R.string.no_index
            }
        }
    }
}