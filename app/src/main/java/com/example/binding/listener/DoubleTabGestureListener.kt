package com.example.binding.listener

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


private const val DOUBLE_TAP_INTERVAL = 300

class DoubleTabTouchListener(
    touchView: View,
) : View.OnTouchListener {

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(
            touchView.context, DoubleTabGestureListener().also {
                it.singleTapConfirmedListener = singleTapConfirmedListener
                it.doubleTapListener = doubleTapListener
            }
        )
    }


    var singleTapConfirmedListener: OnSingleTapConfirmedListener? = null

    fun onSingleTapConfirmedListener(listener: OnSingleTapConfirmedListener) {
        singleTapConfirmedListener = listener
    }

    var doubleTapListener: OnDoubleTapListener? = null

    fun onDoubleTapListener(listener: OnDoubleTapListener) {
        doubleTapListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

}

class DoubleTabGestureListener : GestureDetector.SimpleOnGestureListener() {
    private var down: Long = 0
    private var isConsume = false
    var singleTapConfirmedListener: OnSingleTapConfirmedListener? = null
    var doubleTapListener: OnDoubleTapListener? = null

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        if (isConsume) {
            isConsume = false
            return true
        }

        return singleTapConfirmedListener?.onSingleTapConfirmed(e)
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
        doubleTapListener?.onDoubleTap(e)
    }
}

fun interface OnSingleTapConfirmedListener {
    fun onSingleTapConfirmed(e: MotionEvent): Boolean
}

fun interface OnDoubleTapListener {
    fun onDoubleTap(e: MotionEvent)
}
