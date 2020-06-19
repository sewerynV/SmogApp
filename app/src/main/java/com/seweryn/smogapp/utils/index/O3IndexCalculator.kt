package com.seweryn.smogapp.utils.index

class O3IndexCalculator : IndexCalculator() {
    override val veryGoodMaxValue: Float = 70f
    override val goodMaxValue: Float = 120f
    override val moderateMaxValue: Float = 150f
    override val sufficientMaxValue: Float = 180f
    override val badMaxValue: Float = 240f

}