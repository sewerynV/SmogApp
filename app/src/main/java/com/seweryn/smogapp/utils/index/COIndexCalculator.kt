package com.seweryn.smogapp.utils.index

class COIndexCalculator : IndexCalculator() {
    override val veryGoodMaxValue: Float = 3000f
    override val goodMaxValue: Float = 7000f
    override val moderateMaxValue: Float = 11000f
    override val sufficientMaxValue: Float = 15000f
    override val badMaxValue: Float = 21000f

}