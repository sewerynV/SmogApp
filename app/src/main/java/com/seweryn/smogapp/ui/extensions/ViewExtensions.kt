package com.seweryn.smogapp.ui.extensions

import android.view.View

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.showConditionally(isVisible: Boolean) {
    if(isVisible) this.show() else this.hide()
}

fun View.fadeAndZoomIn() {
    alpha = 0f
    scaleX = 0f
    scaleY = 0f
    animate()
        .alpha(1f)
        .scaleX(1f).scaleY(1f)
        .setDuration(3000)
        .start();
}