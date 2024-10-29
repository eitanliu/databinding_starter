package com.eitanliu.utils

import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.core.view.updateLayoutParams
import com.eitanliu.binding.adapter.viewExtController
import com.eitanliu.binding.model.FitWindowInsets
import com.eitanliu.binding.annotation.FitInsetsMode

object WindowInsetsUtil {
    @JvmOverloads
    fun View.fitWindowInsets(
        windowInsets: WindowInsetsCompat,
        fitSystemBar: Boolean? = null,
        fitStatusBars: Boolean? = null,
        fitNavigationBars: Boolean? = null,
        fitCaptionBar: Boolean? = null,
        fitDisplayCutout: Boolean? = null,
        fitHorizontal: Boolean? = null,
        fitMergeType: Boolean? = null,
        @InsetsType type: Int? = null,
        @FitInsetsMode mode: Int? = null,
    ) {
        val status = type != null || fitSystemBar == true
                || fitStatusBars == true || fitNavigationBars == true
                || fitCaptionBar == true || fitDisplayCutout == true
                || fitHorizontal == true

        var insetsType: Int? = null
        if (fitStatusBars == true) {
            insetsType = WindowInsetsCompat.Type.statusBars()
        }
        if (fitNavigationBars == true) {
            insetsType = WindowInsetsCompat.Type.navigationBars() or (insetsType ?: 0)
        }
        if (fitCaptionBar == true) {
            insetsType = WindowInsetsCompat.Type.captionBar() or (insetsType ?: 0)
        }
        if (fitDisplayCutout == true) {
            insetsType = WindowInsetsCompat.Type.displayCutout() or (insetsType ?: 0)
        }
        if (fitSystemBar == true) {
            insetsType = WindowInsetsCompat.Type.systemBars() or (insetsType ?: 0)
        }
        insetsType = when {
            type == null -> insetsType
            fitMergeType == true -> type or (insetsType ?: 0)
            else -> type
        }

        val cacheModel = viewExtController.fitWindowInsets
        val cacheStatus = cacheModel?.status
        val cacheMode = cacheModel?.mode
        val cacheType = cacheModel?.type
        val cacheInsets = cacheModel?.insets
        val fitMode = mode ?: cacheMode ?: FitInsetsMode.MARGIN
        val fitType = insetsType
        val fitInsets = fitType?.let { windowInsets.getInsets(it) } ?: Insets.NONE
        val insets = if (fitHorizontal == true) {
            val horizontalInsets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val fitHorizontalInsets = Insets.of(horizontalInsets.left, 0, horizontalInsets.right, 0)
            Insets.max(fitHorizontalInsets, fitInsets)
        } else fitInsets
        // Log.e("fitSystemBar", "$status $cacheStatus, $fitMode $cacheMode, $insetsType $cacheType")
        // Log.e("fitSystemBarInsets", "apply: $insets, \ncache: $cacheInsets")
        if (cacheStatus == null && !status) return // 跳过未设置
        if (status == cacheStatus && cacheInsets == insets) return // 跳过未变化
        val model = FitWindowInsets(status, fitMode, fitType, insets)
        updateLayoutParams {
            if (fitMode == FitInsetsMode.MARGIN && this is ViewGroup.MarginLayoutParams) {
                var left = leftMargin
                var top = topMargin
                var right = rightMargin
                var bottom = bottomMargin
                if (cacheStatus == true && cacheInsets != null) {
                    if (cacheMode == FitInsetsMode.MARGIN) {
                        left -= cacheInsets.left
                        top -= cacheInsets.top
                        right -= cacheInsets.right
                        bottom -= cacheInsets.bottom
                    } else {
                        if (height > 0) height -= insets.top + insets.bottom
                        setPadding(
                            paddingLeft - cacheInsets.left, paddingTop - cacheInsets.top,
                            paddingRight - cacheInsets.right, paddingBottom - cacheInsets.top,
                        )
                    }
                }
                if (status) {
                    leftMargin = left + insets.left
                    topMargin = top + insets.top
                    rightMargin = right + insets.right
                    bottomMargin = bottom + insets.bottom
                } else {
                    leftMargin = left
                    topMargin = top
                    rightMargin = right
                    bottomMargin = bottom
                }
            } else {
                var left = paddingLeft
                var top = paddingTop
                var right = paddingRight
                var bottom = paddingBottom
                if (cacheStatus == true && cacheInsets != null) {
                    if (cacheMode == FitInsetsMode.MARGIN && this is ViewGroup.MarginLayoutParams) {
                        leftMargin -= cacheInsets.left
                        topMargin -= cacheInsets.top
                        rightMargin -= cacheInsets.right
                        bottomMargin -= cacheInsets.bottom
                    } else {
                        left -= cacheInsets.left
                        top -= cacheInsets.top
                        right -= cacheInsets.right
                        bottom -= cacheInsets.bottom
                        if (height > 0) height -= cacheInsets.top + cacheInsets.bottom
                    }
                }
                if (status) {
                    if (height > 0) height += insets.top + insets.bottom
                    setPadding(
                        left + insets.left, top + insets.top,
                        right + insets.right, bottom + insets.bottom
                    )
                } else {
                    setPadding(left, top, right, bottom)
                }
            }
        }
        viewExtController.fitWindowInsets = model
    }

    fun View.getWindowInsetsController(
        window: Window
    ) = viewExtController.getWindowInsetsController(window)

}