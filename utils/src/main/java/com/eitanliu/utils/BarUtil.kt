package com.eitanliu.utils

import android.os.Build
import android.view.Window
import android.view.WindowManager

object BarUtil {

    fun Window.setNavBar(isLight: Boolean, color: Int?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isNavigationBarContrastEnforced = false
        }
        isAppearanceLightNavigationBars = isLight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color != null) navigationBarColor = color
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) == 0) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }

        // val decorView = decorView
        // val vis = decorView.systemUiVisibility
        // val option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        //         View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        //         View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // decorView.systemUiVisibility = vis or option
    }
}