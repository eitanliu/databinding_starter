package com.eitanliu.utils

import android.view.Window
import androidx.core.view.WindowInsetsCompat
import com.eitanliu.utils.WindowInsetsUtil.getWindowInsetsController

class WindowExt

inline var Window.isAppearanceLightStatusBars
    get() = decorView.getWindowInsetsController(this).isAppearanceLightStatusBars
    set(value) {
        decorView.getWindowInsetsController(this).isAppearanceLightStatusBars = value
    }

inline var Window.isAppearanceLightNavigationBars
    get() = decorView.getWindowInsetsController(this).isAppearanceLightNavigationBars
    set(value) {
        decorView.getWindowInsetsController(this).isAppearanceLightNavigationBars = value
    }

val Window.isShowSoftwareKeyboard
    get() = decorView.rootWindowInsetsCompat?.isShowSoftwareKeyboard

inline val Window.systemBarsInsets
    get() = decorView.rootWindowInsetsCompat?.systemBarsInsets

inline val Window.statusBarsInsets
    get() = decorView.rootWindowInsetsCompat?.statusBarsInsets

inline val Window.navigationBarsInsets
    get() = decorView.rootWindowInsetsCompat?.navigationBarsInsets

val WindowInsetsCompat.isShowSoftwareKeyboard: Boolean
    get() {
        val imeBottom = imeInsets.bottom
        val navBottom = navigationBarsInsets.bottom
        return imeBottom > navBottom
    }

inline val WindowInsetsCompat.systemBarsInsets
    get() = getInsets(WindowInsetsCompat.Type.systemBars())

inline val WindowInsetsCompat.statusBarsInsets
    get() = getInsets(WindowInsetsCompat.Type.statusBars())

inline val WindowInsetsCompat.navigationBarsInsets
    get() = getInsets(WindowInsetsCompat.Type.navigationBars())

inline val WindowInsetsCompat.imeInsets
    get() = getInsets(WindowInsetsCompat.Type.ime())
