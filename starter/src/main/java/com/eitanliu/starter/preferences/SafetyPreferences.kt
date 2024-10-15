package com.eitanliu.starter.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.IntDef
import androidx.annotation.StringDef

interface SafetyPreferences : SharedPreferences {

    fun getBytes(key: String, defValue: ByteArray?): ByteArray?

    fun getDouble(key: String, defValue: Double): Double

    fun containsForType(key: String?, @Type type: String): Boolean

    fun allKeyType(key: String) = Type.allType.map { key.toKeyType(it) }

    override fun edit(): Editor

    interface Editor : SharedPreferences.Editor {
        fun putBytes(key: String, value: ByteArray?): Editor

        fun putDouble(key: String, value: Double): Editor

        fun removeForType(key: String, @Type type: String): Editor
    }

    companion object {
        const val SEPARATOR = "@_@"
        const val SEPARATOR_SIZE = SEPARATOR.length

        fun String.parserKeyType() = run {
            val suffixIndex = lastIndexOf(SEPARATOR)
            if (suffixIndex <= 0) return@run this to Type.UNKNOWN
            substring(0, suffixIndex) to substring(suffixIndex + SEPARATOR_SIZE)
        }

        fun String.toKeyType(@Type type: String) = "$this$SEPARATOR$type"
    }

    @StringDef(
        Type.UNKNOWN, Type.BOOLEAN, Type.INT, Type.LONG, Type.FLOAT, Type.DOUBLE,
        Type.STRING, Type.STRING_SET, Type.BYTE_ARRAY
    )
    annotation class Type {
        companion object {
            const val UNKNOWN = "0"
            const val BOOLEAN = "1"
            const val INT = "2"
            const val LONG = "3"
            const val FLOAT = "4"
            const val DOUBLE = "5"
            const val STRING = "6"
            const val STRING_SET = "7"
            const val BYTE_ARRAY = "8"

            val allType = arrayOf(
                BOOLEAN, INT, LONG, FLOAT, DOUBLE, STRING, STRING_SET, BYTE_ARRAY,
            )

            inline fun <reified T> getType(): String {

                return when (T::class) {
                    Boolean::class -> BOOLEAN
                    Int::class -> INT
                    Long::class -> LONG
                    Float::class -> FLOAT
                    Double::class -> DOUBLE
                    String::class -> STRING
                    Set::class -> STRING_SET
                    ByteArray::class -> BYTE_ARRAY
                    else -> UNKNOWN
                }
            }
        }
    }
}

interface SharedPreferencesDelegate : SharedPreferences {
    val base: SharedPreferences
}

@IntDef(
    PreferencesMode.MODE_PRIVATE,
    PreferencesMode.MODE_MULTI_PROCESS
)
annotation class PreferencesMode {
    companion object {
        @Suppress("DEPRECATION")
        const val MODE_MULTI_PROCESS = Context.MODE_MULTI_PROCESS
        const val MODE_PRIVATE = Context.MODE_PRIVATE
    }
}
