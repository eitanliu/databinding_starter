package com.eitanliu.utils

import android.util.Log
import androidx.annotation.IntDef
import kotlin.math.min

@Suppress("NOTHING_TO_INLINE")
object Logcat {
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(V, D, I, W, E, A)
    annotation class Level

    const val V: Int = Log.VERBOSE
    const val D: Int = Log.DEBUG
    const val I: Int = Log.INFO
    const val W: Int = Log.WARN
    const val E: Int = Log.ERROR
    const val A: Int = Log.ASSERT

    private const val MAX_LENGTH = 2000

    var level = I
    var tag = "StarterLog"
    var space = "->"
    var isDebug = true

    private inline fun trace(): String {
        // 获取异常所在调用栈信息
        val caller = Throwable().stackTrace.first { it.className != Logcat::class.java.name }
        // 类名 + 方法名
        // var callerClassName = caller.className
        // callerClassName = callerClassName.substring(callerClassName.lastIndexOf(".") + 1);
        // return "${callerClassName}.${caller.methodName}(${caller.fileName}:${caller.lineNumber})"
        // 方法名 + 文件名
        return "${caller.methodName}(${caller.fileName}:${caller.lineNumber})"
    }

    inline fun msg(
        msg: String,
    ) = msg(msg, null, level, tag)

    inline fun msg(
        msg: String, tr: Throwable?,
    ) = msg(msg, tr, level, tag)

    inline fun msg(
        msg: String, tr: Throwable?,
        tag: String, @Level level: Int = Logcat.level,
    ) = msg(msg, tr, level, tag)

    inline fun msg(
        msg: String, tr: Throwable?,
        @Level level: Int, tag: String = Logcat.tag,
    ) = msg(tr, level, tag) { msg }

    fun msg(
        tr: Throwable? = null,
        @Level level: Int = Logcat.level,
        tag: String = Logcat.tag,
        builder: () -> String = { "" }
    ) {
        if (isDebug) {
            val msg = builder()
            val length = msg.length
            var index = 1
            var start = 0
            var end = min(length, MAX_LENGTH)
            val lines = length / MAX_LENGTH
            val pad = lines / 10 + 2

            do {
                val p = if (lines > 2) "$index ".padStart(pad, '0') else ""
                val ctx = "${trace()} $p$space ${msg.subSequence(start, end)}"
                when (level) {
                    V -> Log.v(tag, ctx, tr)
                    D -> Log.d(tag, ctx, tr)
                    I -> Log.i(tag, ctx, tr)
                    W -> Log.w(tag, ctx, tr)
                    E -> Log.e(tag, ctx, tr)
                    else -> {
                        Log.println(A, tag, ctx)
                        if (tr != null) Log.println(A, tag, Log.getStackTraceString(tr))
                    }
                }

                start = end
                end = min(length, end + MAX_LENGTH)
                index += 1
            } while (start < end)
        }
    }
}