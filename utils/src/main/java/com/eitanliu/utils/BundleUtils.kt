package com.eitanliu.utils

import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Size
import android.util.SizeF
import java.io.Serializable

/**
 * [androidx.core.os.bundleOf]
 * [androidx.core.os.persistableBundleOf]
 */
@Suppress("KotlinConstantConditions", "ObsoleteSdkInt", "NOTHING_TO_INLINE")
object BundleUtils {

    fun Bundle.putObject(key: String?, value: Any?) {
        when (value) {
            null -> putString(key, null) // Any nullable type will suffice.

            // Scalars
            is Boolean -> putBoolean(key, value)
            is Byte -> putByte(key, value)
            is Char -> putChar(key, value)
            is Double -> putDouble(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Short -> putShort(key, value)

            // References
            is Bundle -> putBundle(key, value)
            is CharSequence -> putCharSequence(key, value)
            is Parcelable -> putParcelable(key, value)

            // Scalar arrays
            is BooleanArray -> putBooleanArray(key, value)
            is ByteArray -> putByteArray(key, value)
            is CharArray -> putCharArray(key, value)
            is DoubleArray -> putDoubleArray(key, value)
            is FloatArray -> putFloatArray(key, value)
            is IntArray -> putIntArray(key, value)
            is LongArray -> putLongArray(key, value)
            is ShortArray -> putShortArray(key, value)

            // Reference arrays
            is Array<*> -> {
                val componentType = value::class.java.componentType!!
                @Suppress("UNCHECKED_CAST") // Checked by reflection.
                when {
                    Parcelable::class.java.isAssignableFrom(componentType) -> {
                        putParcelableArray(key, value as Array<Parcelable>)
                    }

                    String::class.java.isAssignableFrom(componentType) -> {
                        putStringArray(key, value as Array<String>)
                    }

                    CharSequence::class.java.isAssignableFrom(componentType) -> {
                        putCharSequenceArray(key, value as Array<CharSequence>)
                    }

                    Serializable::class.java.isAssignableFrom(componentType) -> {
                        putSerializable(key, value)
                    }

                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                            "Illegal value array type $valueType for key \"$key\""
                        )
                    }
                }
            }

            // Last resort. Also we must check this after Array<*> as all arrays are serializable.
            is Serializable -> putSerializable(key, value)

            else -> {
                if (value is IBinder) {
                    this.putBinder(key, value)
                } else if (Build.VERSION.SDK_INT >= 21 && value is Size) {
                    putSize(key, value)
                } else if (Build.VERSION.SDK_INT >= 21 && value is SizeF) {
                    putSizeF(key, value)
                } else {
                    val valueType = value.javaClass.canonicalName
                    throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
                }
            }
        }
    }

    fun PersistableBundle.putObject(key: String?, value: Any?) {
        when (value) {
            null -> putString(key, null) // Any nullable type will suffice.

            // Scalars
            is Boolean -> {
                putBoolean(key, value)
                if (Build.VERSION.SDK_INT >= 22) {
                } else {
                    throw IllegalArgumentException(
                        "Illegal value type boolean for key \"$key\""
                    )
                }
            }

            is Double -> putDouble(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)

            // References
            is String -> putString(key, value)

            // Scalar arrays
            is BooleanArray -> {
                if (Build.VERSION.SDK_INT >= 22) {
                    putBooleanArray(key, value)
                } else {
                    throw IllegalArgumentException(
                        "Illegal value type boolean[] for key \"$key\""
                    )
                }
            }

            is DoubleArray -> putDoubleArray(key, value)
            is IntArray -> putIntArray(key, value)
            is LongArray -> putLongArray(key, value)

            // Reference arrays
            is Array<*> -> {
                val componentType = value::class.java.componentType!!
                @Suppress("UNCHECKED_CAST") // Checked by reflection.
                when {
                    String::class.java.isAssignableFrom(componentType) -> {
                        putStringArray(key, value as Array<String>)
                    }

                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                            "Illegal value array type $valueType for key \"$key\""
                        )
                    }
                }
            }

            else -> {
                val valueType = value.javaClass.canonicalName
                throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
            }
        }
    }

    inline fun BaseBundle.toArray() = toList().toTypedArray()

    inline fun BaseBundle.toList() = toMap().toList()

    @Suppress("DEPRECATION")
    fun BaseBundle.toMap(): Map<String, Any?> {
        val state: MutableMap<String, Any?> = HashMap()
        for (key in keySet()) {
            state[key] = this[key]
        }
        return state
    }

}
