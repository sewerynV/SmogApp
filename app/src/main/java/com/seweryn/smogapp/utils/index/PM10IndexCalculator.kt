package com.seweryn.smogapp.utils.index

class PM10IndexCalculator : IndexCalculator() {
    override val veryGoodMaxValue: Float = 20f
    override val goodMaxValue: Float = 50f
    override val moderateMaxValue: Float = 80f
    override val sufficientMaxValue: Float = 110f
    override val badMaxValue: Float = 150f

}