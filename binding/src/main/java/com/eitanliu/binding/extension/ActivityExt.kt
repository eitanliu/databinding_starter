package com.eitanliu.binding.extension

import android.app.Activity
import com.eitanliu.utils.isAppearanceLightNavigationBars
import com.eitanliu.utils.isAppearanceLightStatusBars
import com.eitanliu.utils.navigationBarsInsets
import com.eitanliu.utils.statusBarsInsets
import com.eitanliu.utils.systemBarsInsets

class ActivityExt

inline val <T : Activity> T.selfActivity get() = this

inline var Activity.isAppearanceLightStatusBars
    get() = window.isAppearanceLightStatusBars
    set(value) {
        window.isAppearanceLightStatusBars = value
    }

inline var Activity.isAppearanceLightNavigationBars
    get() = window.isAppearanceLightNavigationBars
    set(value) {
        window.isAppearanceLightNavigationBars = value
    }

inline val Activity.systemBarsInsets
    get() = window.systemBarsInsets

inline val Activity.statusBarsInsets
    get() = window.statusBarsInsets

inline val Activity.navigationBarsInsets
    get() = window.navigationBarsInsets