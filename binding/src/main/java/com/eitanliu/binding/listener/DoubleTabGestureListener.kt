package com.eitanliu.binding.listener

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


private const val DOUBLE_TAP_INTERVAL = 300

class DoubleTabTouchListener(
    touchView: View,
) : DoubleTabGestureListener(touchView.context), View.OnTouchListener {

    fun onSingleTapConfirmedListener(listener: OnSingleTapConfirmedListener) {
        onSingleTapConfirmedListener = listener
    }

    fun onDoubleTapListener(listener: OnDoubleTapListener) {
        onDoubleTapListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

}

open class DoubleTabGestureListener(
    context: Context,
) : GestureDetector.SimpleOnGestureListener() {
    private var down: Long = 0
    private var isConsume = false
    var onSingleTapConfirmedListener: OnSingleTapConfirmedListener? = null
    var onDoubleTapListener: OnDoubleTapListener? = null

    open val gestureDetector: GestureDetector by lazy {
        GestureDetector(context, this)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        if (isConsume) {
            isConsume = false
            return true
        }

        return onSingleTapConfirmedListener?.onSingleTapConfirmed(e)
            ?: super.onSingleTapConfirmed(e)
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        val l = SystemClock.uptimeMillis() - down
        if (l <= DOUBLE_TAP_INTERVAL) {
            extraDoubleTap(e)
            isConsume = true
            down = SystemClock.uptimeMillis()
            return true
        }
        return super.onSingleTapUp(e)
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        extraDoubleTap(e)
        down = SystemClock.uptimeMillis()
        return true
    }

    private fun extraDoubleTap(e: MotionEvent) {
        onDoubleTapListener?.onDoubleTap(e)
    }
}

fun interface OnSingleTapConfirmedListener {
    fun onSingleTapConfirmed(e: MotionEvent): Boolean
}

fun interface OnDoubleTapListener {
    fun onDoubleTap(e: MotionEvent)
}
