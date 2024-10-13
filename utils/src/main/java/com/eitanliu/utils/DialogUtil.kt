@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Window
import android.view.WindowManager

object DialogUtil {
    // 无标题栏
    inline fun Dialog.noTitle() = apply {
        window?.addFlags(Window.FEATURE_NO_TITLE)
    }

    // 透明状态栏
    inline fun Dialog.translucentStatus() = apply {
        window?.apply {
            val version = Build.VERSION.SDK_INT
            if (version >= Build.VERSION_CODES.KITKAT) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    // 透明导航栏
    inline fun Dialog.translucentNavigation() = apply {
        window?.apply {
            val version = Build.VERSION.SDK_INT
            if (version >= Build.VERSION_CODES.KITKAT) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    // 透明窗口
    inline fun Dialog.translucentWindows() = apply {
        window?.apply {
            val version = Build.VERSION.SDK_INT
            if (version >= Build.VERSION_CODES.KITKAT) {
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
        }
    }

    // 透明背景
    inline fun Dialog.transparentBackground() = apply {
        window?.apply {
            setDimAmount(0f) //阴影
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //透明背景
        }
    }

    // 全屏
    inline fun Dialog.fillWindow() = apply {
        window?.apply {
            decorView.setPadding(0, 0, 0, 0)
            attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
            //attributes = attributes
        }
    }
}