package com.seweryn.smogapp.utils

import java.text.DecimalFormat

fun Float.toStringTrimmingDecimalPlaces(): String {
    return DecimalFormat("#.##").format(this)
}