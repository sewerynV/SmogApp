package com.seweryn.smogapp.utils.index

class NO2IndexCalculator : IndexCalculator() {
    override val veryGoodMaxValue: Float = 40f
    override val goodMaxValue: Float = 100f
    override val moderateMaxValue: Float = 150f
    override val sufficientMaxValue: Float = 200f
    override val badMaxValue: Float = 400f

}