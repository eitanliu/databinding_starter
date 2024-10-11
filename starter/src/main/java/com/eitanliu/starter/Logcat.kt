package com.eitanliu.starter

import android.util.Log
import androidx.annotation.IntRange
import kotlin.math.min

@Suppress("NOTHING_TO_INLINE")
object Logcat {
    @Retention(AnnotationRetention.SOURCE)
    @IntRange(from = 2, to = 7)
    annotation class Level {
        companion object {
            const val V: Int = Log.VERBOSE
            const val D: Int = Log.DEBUG
            const val I: Int = Log.INFO
            const val W: Int = Log.WARN
            const val E: Int = Log.ERROR
            const val A: Int = Log.ASSERT
        }
    }

    private const val MAX_LENGTH = 2000

    var level = Level.I
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
        tag: String, @Level level: Int = this.level,
    ) = msg(msg, tr, level, tag)

    inline fun msg(
        msg: String, tr: Throwable?,
        @Level level: Int, tag: String = this.tag,
    ) = msg(tr, level, tag) { msg }

    fun msg(
        tr: Throwable? = null,
        @Level level: Int = this.level,
        tag: String = this.tag,
        builder: () -> String
    ) {
        if (isDebug) {
            val msg = builder()
            val length = msg.length
            var index = 0
            var start = 0
            var end = min(length, MAX_LENGTH)
            val pad = length / MAX_LENGTH / 10 + 2

            do {
                val p = if (pad > 2) "$index ".padStart(pad, '0') else ""
                val ctx = "${trace()} $p$space ${msg.subSequence(start, end)}"
                when (level) {
                    Level.V -> Log.v(tag, ctx, tr)
                    Level.D -> Log.d(tag, ctx, tr)
                    Level.I -> Log.i(tag, ctx, tr)
                    Level.W -> Log.w(tag, ctx, tr)
                    Level.E -> Log.e(tag, ctx, tr)
                    else -> {
                        Log.println(Level.A, tag, ctx)
                        if (tr != null) Log.println(Level.A, tag, Log.getStackTraceString(tr))
                    }
                }

                start = end
                end = min(length, end + MAX_LENGTH)
                index += 1
            } while (start < end)
        }
    }
}