package com.example.binding.adapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

class ConstraintLayoutAdapter

@BindingAdapter(
    "layout_constraintLeft_toLeftOf", "layout_constraintLeft_toRightOf",
    "layout_constraintRight_toLeftOf", "layout_constraintRight_toRightOf",
    "layout_constraintTop_toTopOf", "layout_constraintTop_toBottomOf",
    "layout_constraintBottom_toTopOf", "layout_constraintBottom_toBottomOf",
    "layout_constraintStart_toStartOf", "layout_constraintStart_toEndOf",
    "layout_constraintEnd_toStartOf", "layout_constraintEnd_toEndOf",
    requireAll = false
)
fun View.setConstraintLayoutParams(
    leftToLeft: Int? = null, leftToRight: Int? = null,
    rightToLeft: Int? = null, rightToRight: Int? = null,
    topToTop: Int? = null, topToBottom: Int? = null,
    bottomToTop: Int? = null, bottomToBottom: Int? = null,
    startToStart: Int? = null, startToEnd: Int? = null,
    endToStart: Int? = null, endToEnd: Int? = null,
) {
    (layoutParams as? ConstraintLayout.LayoutParams)?.also { layoutParams ->
        val oldLayoutParams = ConstraintLayout.LayoutParams(layoutParams)
        if (leftToLeft != null) layoutParams.leftToLeft = leftToLeft
        if (leftToRight != null) layoutParams.leftToRight = leftToRight
        if (rightToLeft != null) layoutParams.rightToLeft = rightToLeft
        if (rightToRight != null) layoutParams.rightToRight = rightToRight
        if (topToTop != null) layoutParams.topToTop = topToTop
        if (topToBottom != null) layoutParams.topToBottom = topToBottom
        if (bottomToTop != null) layoutParams.bottomToTop = bottomToTop
        if (bottomToBottom != null) layoutParams.bottomToBottom = bottomToBottom
        if (startToStart != null) layoutParams.startToStart = startToStart
        if (startToEnd != null) layoutParams.startToEnd = startToEnd
        if (endToStart != null) layoutParams.endToStart = endToStart
        if (endToEnd != null) layoutParams.endToEnd = endToEnd

        if (
            layoutParams.leftToLeft != oldLayoutParams.leftToLeft ||
            layoutParams.leftToRight != oldLayoutParams.leftToLeft ||
            layoutParams.rightToLeft != oldLayoutParams.rightToLeft ||
            layoutParams.rightToRight != oldLayoutParams.rightToRight ||
            layoutParams.topToTop != oldLayoutParams.topToTop ||
            layoutParams.topToBottom != oldLayoutParams.topToBottom ||
            layoutParams.bottomToTop != oldLayoutParams.bottomToTop ||
            layoutParams.bottomToBottom != oldLayoutParams.bottomToBottom ||
            layoutParams.startToStart != oldLayoutParams.startToStart ||
            layoutParams.startToEnd != oldLayoutParams.startToEnd ||
            layoutParams.endToStart != oldLayoutParams.endToStart
        ) {
            setLayoutParams(layoutParams)
        }
    }
}

@BindingAdapter("layout_constraintDimensionRatio")
fun View.setDimensionRatio(ratio: Float) {
    val params = (layoutParams as ConstraintLayout.LayoutParams)
    params.dimensionRatio = ratio.toString()
    this.layoutParams = params
}

@BindingAdapter("layout_constraintDimensionRatio")
fun View.setDimensionRatio(ratio: String) {
    val params = (layoutParams as ConstraintLayout.LayoutParams)
    params.dimensionRatio = ratio
    this.layoutParams = params
}