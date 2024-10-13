@file:Suppress("HasPlatformType", "MemberVisibilityCanBePrivate")

package com.eitanliu.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.eitanliu.utils.IntentUtil.clearTops
import kotlin.system.exitProcess

open class CrashHandler<T>(
    val context: Context, val activityClass: Class<T>
) : Thread.UncaughtExceptionHandler, DelegateUncaughtExceptionHandler {
    var delay = 100
    var restart = true

    override val delegateHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (handleException(e)) {
            if (restart) {
                val intent = Intent(context, activityClass).putExtra("crash", true).clearTops()
                val flags = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                val pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, flags
                )
                val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                am.set(AlarmManager.RTC, System.currentTimeMillis() + delay, pendingIntent)
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(0)
            } else {
                delegateHandler?.uncaughtException(t, e)
            }
        } else {
            delegateHandler?.uncaughtException(t, e)
        }
    }

    open fun handleException(e: Throwable): Boolean {
        //runOnUi { context.toast("很抱歉,程序出现异常即将退出") }
        return true
    }
}

/**
 * example : [Thread.setDefaultUncaughtExceptionHandler]
 * Thread.setDefaultUncaughtExceptionHandler(CrashLogHandler())
 */
open class CrashLogHandler : Thread.UncaughtExceptionHandler, DelegateUncaughtExceptionHandler {

    override val delegateHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        Logcat.msg("Crash: ${t.name}", e, Logcat.Level.E)
        delegateHandler?.uncaughtException(t, e)
    }
}

interface DelegateUncaughtExceptionHandler {
    val delegateHandler: Thread.UncaughtExceptionHandler
}