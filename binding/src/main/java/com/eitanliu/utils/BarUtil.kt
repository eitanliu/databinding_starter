package com.eitanliu.utils

import android.graphics.Color
import android.os.Build
import android.view.Window
import android.view.WindowManager

object BarUtil {


    fun Window.setStatusBar(isLight: Boolean?, color: Int?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isNavigationBarContrastEnforced = false
        }
        if (isLight != null) isAppearanceLightStatusBars = isLight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = color ?: Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) == 0) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    fun Window.setNavBar(isLight: Boolean?, color: Int?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isNavigationBarContrastEnforced = false
        }
        if (isLight != null) isAppearanceLightNavigationBars = isLight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navigationBarColor = color ?: Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) == 0) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }
}