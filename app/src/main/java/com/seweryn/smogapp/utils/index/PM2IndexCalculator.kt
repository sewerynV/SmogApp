package com.seweryn.smogapp.utils.index

class PM2IndexCalculator : IndexCalculator() {
    override val veryGoodMaxValue: Float = 13f
    override val goodMaxValue: Float = 35f
    override val moderateMaxValue: Float = 55f
    override val sufficientMaxValue: Float = 75f
    override val badMaxValue: Float = 110f

}