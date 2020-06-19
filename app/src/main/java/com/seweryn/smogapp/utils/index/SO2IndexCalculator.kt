package com.seweryn.smogapp.utils.index

class SO2IndexCalculator : IndexCalculator() {
    override val veryGoodMaxValue: Float = 50f
    override val goodMaxValue: Float = 100f
    override val moderateMaxValue: Float = 200f
    override val sufficientMaxValue: Float = 350f
    override val badMaxValue: Float = 500f

}