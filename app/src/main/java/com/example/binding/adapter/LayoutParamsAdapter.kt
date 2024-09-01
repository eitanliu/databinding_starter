package com.example.binding.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

class LayoutParamsAdapter

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
    val lp = (layoutParams as? ConstraintLayout.LayoutParams) ?: return
    val old = ConstraintLayout.LayoutParams(lp)
    if (leftToLeft != null) lp.leftToLeft = leftToLeft
    if (leftToRight != null) lp.leftToRight = leftToRight
    if (rightToLeft != null) lp.rightToLeft = rightToLeft
    if (rightToRight != null) lp.rightToRight = rightToRight
    if (topToTop != null) lp.topToTop = topToTop
    if (topToBottom != null) lp.topToBottom = topToBottom
    if (bottomToTop != null) lp.bottomToTop = bottomToTop
    if (bottomToBottom != null) lp.bottomToBottom = bottomToBottom
    if (startToStart != null) lp.startToStart = startToStart
    if (startToEnd != null) lp.startToEnd = startToEnd
    if (endToStart != null) lp.endToStart = endToStart
    if (endToEnd != null) lp.endToEnd = endToEnd

    if (!lp.equalsConstraint(old)) {
        this.layoutParams = lp
    }
}

@BindingAdapter("layout_constraintDimensionRatio")
fun View.setDimensionRatio(ratio: Float) {
    val lp = (layoutParams as? ConstraintLayout.LayoutParams) ?: return
    val old = ConstraintLayout.LayoutParams(lp)
    lp.dimensionRatio = ratio.toString()
    if (!lp.equalsConstraint(old)) {
        this.layoutParams = lp
    }
}

@BindingAdapter("layout_constraintDimensionRatio")
fun View.setDimensionRatio(ratio: String) {
    val lp = (layoutParams as? ConstraintLayout.LayoutParams) ?: return
    val old = ConstraintLayout.LayoutParams(lp)
    lp.dimensionRatio = ratio
    if (!lp.equalsConstraint(old)) {
        this.layoutParams = lp
    }
}

/**
 * 设置goneMargin
 */
@BindingAdapter(
    "layout_goneMarginLeft", "layout_goneMarginTop",
    "layout_goneMarginRight", "layout_goneMarginBottom",
    "layout_goneMarginStart", "layout_goneMarginEnd",
    "layout_goneMarginBaseline",
    requireAll = false
)
fun View.setGoneMargin(
    goneMarginLeft: Int? = null, goneMarginTop: Int? = null,
    goneMarginRight: Int? = null, goneMarginBottom: Int? = null,
    goneMarginStart: Int? = null, goneMarginEnd: Int? = null,
    goneMarginBaseline: Int? = null,
) {
    val lp = this.layoutParams as? ConstraintLayout.LayoutParams ?: return
    val old = ConstraintLayout.LayoutParams(lp)
    if (goneMarginLeft != null) lp.goneLeftMargin = goneMarginLeft
    if (goneMarginTop != null) lp.goneTopMargin = goneMarginTop
    if (goneMarginRight != null) lp.goneRightMargin = goneMarginRight
    if (goneMarginBottom != null) lp.goneBottomMargin = goneMarginBottom
    if (goneMarginStart != null) lp.goneStartMargin = goneMarginStart
    if (goneMarginEnd != null) lp.goneEndMargin = goneMarginEnd
    if (goneMarginBaseline != null) lp.goneBaselineMargin = goneMarginBaseline
    if (!lp.equalsConstraint(old)) {
        this.layoutParams = lp
    }
}

/**
 * 设置margin
 */
@BindingAdapter(
    "layout_marginLeft", "layout_marginTop",
    "layout_marginRight", "layout_marginBottom",
    "layout_marginStart", "layout_marginEnd",
    "layout_width", "layout_height",
    "layoutDirection",
    requireAll = false,
)
fun View.setMargin(
    marginLeft: Int? = null, marginTop: Int? = null,
    marginRight: Int? = null, marginBottom: Int? = null,
    marginStart: Int? = null, marginEnd: Int? = null,
    width: Int? = null, height: Int? = null,
    layoutDirection: Int? = null,
) {
    val lp = this.layoutParams as? ViewGroup.MarginLayoutParams ?: return
    val old = ViewGroup.MarginLayoutParams(lp)
    if (marginLeft != null) lp.leftMargin = marginLeft
    if (marginTop != null) lp.topMargin = marginTop
    if (marginRight != null) lp.rightMargin = marginRight
    if (marginBottom != null) lp.bottomMargin = marginBottom
    if (marginStart != null) lp.marginStart = marginStart
    if (marginEnd != null) lp.marginEnd = marginEnd
    if (width != null) lp.width = width
    if (height != null) lp.height = height
    if (layoutDirection != null) lp.layoutDirection = layoutDirection

    if (!lp.equalsSize(old) || !lp.equalsMargin(old)) this.layoutParams = lp
}

fun (ViewGroup.LayoutParams).equalsSize(other: ViewGroup.LayoutParams): Boolean {
    return !(this.width != other.width || this.height != other.height)
}

fun (ViewGroup.LayoutParams).equalsMargin(other: ViewGroup.LayoutParams): Boolean {
    if (this is ViewGroup.MarginLayoutParams && other is ViewGroup.MarginLayoutParams) {
        if (
            this.leftMargin != other.leftMargin ||
            this.topMargin != other.topMargin ||
            this.rightMargin != other.rightMargin ||
            this.bottomMargin != other.bottomMargin ||
            this.marginStart != other.marginStart ||
            this.marginEnd != other.marginEnd ||
            this.layoutDirection != other.layoutDirection
        ) return false
    }
    return true
}

fun (ViewGroup.LayoutParams).equalsConstraint(other: ViewGroup.LayoutParams): Boolean {
    if (this is ConstraintLayout.LayoutParams && other is ConstraintLayout.LayoutParams) {
        if (
            this.leftToLeft != other.leftToLeft ||
            this.leftToRight != other.leftToLeft ||
            this.rightToLeft != other.rightToLeft ||
            this.rightToRight != other.rightToRight ||
            this.topToTop != other.topToTop ||
            this.topToBottom != other.topToBottom ||
            this.bottomToTop != other.bottomToTop ||
            this.bottomToBottom != other.bottomToBottom ||
            this.startToStart != other.startToStart ||
            this.startToEnd != other.startToEnd ||
            this.endToStart != other.endToStart ||

            this.goneLeftMargin != other.goneLeftMargin ||
            this.goneTopMargin != other.goneTopMargin ||
            this.goneRightMargin != other.goneRightMargin ||
            this.goneBottomMargin != other.goneBottomMargin ||
            this.goneStartMargin != other.goneStartMargin ||
            this.goneEndMargin != other.goneEndMargin ||
            this.goneBaselineMargin != other.goneBaselineMargin ||
            this.dimensionRatio != other.dimensionRatio
        ) return false
    }
    return true
}

/**
 * 设置padding
 *
 * @param paddingLeft Int?
 * @param paddingTop Int?
 * @param paddingRight Int?
 * @param paddingBottom Int?
 */
@BindingAdapter(
    "paddingLeft", "paddingTop", "paddingRight", "paddingBottom",
    requireAll = false,
)
fun View.setPadding(
    paddingLeft: Int? = null,
    paddingTop: Int? = null,
    paddingRight: Int? = null,
    paddingBottom: Int? = null,
) {
    val old = Rect(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom)
    val now = Rect(
        paddingLeft ?: this.paddingLeft, paddingTop ?: this.paddingTop,
        paddingRight ?: this.paddingRight, paddingBottom ?: this.paddingBottom,
    )
    if (old == now) return
    setPadding(now.left, now.top, now.right, now.bottom)
}

@BindingAdapter(
    "paddingStart", "paddingTop", "paddingEnd", "paddingBottom",
    requireAll = false,
)
fun View.setPaddingRelative(
    paddingStart: Int? = null,
    paddingTop: Int? = null,
    paddingEnd: Int? = null,
    paddingBottom: Int? = null,
) {
    val old = Rect(this.paddingStart, this.paddingTop, this.paddingEnd, this.paddingBottom)
    val now = Rect(
        paddingStart ?: this.paddingStart, paddingTop ?: this.paddingTop,
        paddingEnd ?: this.paddingEnd, paddingBottom ?: this.paddingBottom,
    )
    if (old == now) return
    setPaddingRelative(now.left, now.top, now.right, now.bottom)
}
