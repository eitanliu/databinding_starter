package com.eitanliu.starter.utils

import android.os.Build
import android.view.Window
import android.view.WindowManager
import com.eitanliu.binding.extension.isAppearanceLightNavigationBars

object BarUtils {

    fun setNavBar(window: Window, isLight: Boolean, color: Int?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        window.isAppearanceLightNavigationBars = isLight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color != null) window.navigationBarColor = color
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((window.attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) == 0) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }

        // val decorView = window.decorView
        // val vis = decorView.systemUiVisibility
        // val option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        //         View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        //         View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        // decorView.systemUiVisibility = vis or option
    }
}