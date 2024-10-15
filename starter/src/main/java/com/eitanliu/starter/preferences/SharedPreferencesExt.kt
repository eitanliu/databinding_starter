package com.eitanliu.starter.preferences

import android.content.SharedPreferences
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.eitanliu.starter.utils.StringSerializer
import com.eitanliu.utils.asTypeOrNull

class SharedPreferencesExt

/**
 * val a: Int? = sharedPreferences["key"]
 * val a = sharedPreferences.get<Int>("key")
 */
inline operator fun <reified T> SharedPreferences.get(
    key: String
): T? = get(key, null)

/**
 * val a = sharedPreferences["key", 1]
 * val a = sharedPreferences.get("key", 1)
 */
inline operator fun <reified T> SharedPreferences.get(
    key: String, default: T
): T = get(key, default, T::class.java)

@Suppress("UNCHECKED_CAST")
fun <T> SharedPreferences.get(
    key: String, default: T, clazz: Class<T>
): T {
    val res = if (contains(key)) when (clazz) {
        Int::class.java, java.lang.Integer::class.java ->
            getInt(key, default.asTypeOrNull() ?: 0)

        Long::class.java, java.lang.Long::class.java ->
            getLong(key, default?.asTypeOrNull() ?: 0)

        Float::class.java, java.lang.Float::class.java ->
            getFloat(key, default.asTypeOrNull() ?: 0f)

        Double::class.java, java.lang.Double::class.java -> {
            val def = default.asTypeOrNull() ?: 0.0
            if (this is SafetyPreferences) {
                getDouble(key, def)
            } else {
                val value = getLong(key, def.toRawBits())
                Double.fromBits(value)
            }
        }

        Boolean::class.java, java.lang.Boolean::class.java ->
            getBoolean(key, default.asTypeOrNull() ?: false)

        java.lang.String::class.java -> getString(key, default.asTypeOrNull())
        else -> throw IllegalArgumentException()
    } as T else default
    return res
}

/**
 * sharedPreferences["key"] = 2
 * sharedPreferences.set("key", 2)
 */
operator fun <T> SharedPreferences.set(key: String, value: T) {
    with(edit()) {
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> {
                if (this is SafetyPreferences.Editor) {
                    putDouble(key, value)
                } else {
                    putLong(key, value.toRawBits())
                }
            }

            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            null -> remove(key)
            else -> throw IllegalArgumentException()
        }
        apply()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> SharedPreferences.property(
    default: T, key: String? = null,
    noinline onUpdate: ((value: T) -> Unit)? = null
) = SharedProperty(default, this, key, StringSerializer.Json(), onUpdate)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> SharedPreferences.propertyOrNull(
    default: T? = null, key: String? = null,
    noinline onUpdate: ((value: T?) -> Unit)? = null
) = property(default, key, onUpdate)

inline fun <reified T> SharedPreferences.jsonProperty(
    default: T, key: String? = null,
    noinline onUpdate: ((value: T) -> Unit)? = null
) = property(default, key, onUpdate)

inline fun <reified T> SharedPreferences.jsonPropertyOrNull(
    default: T? = null, key: String? = null,
    noinline onUpdate: ((value: T?) -> Unit)? = null
) = property(default, key, onUpdate)

fun SharedPreferences.onChangeListener(
    owner: LifecycleOwner, listener: (key: String?) -> Unit
) {
    owner.lifecycle.addObserver(OnChangeLifecycleListener(this, listener))
}

fun SharedPreferences.onChangeListener(
    owner: ViewModel, listener: (key: String?) -> Unit
) {
    owner.addCloseable(onChangeListener(listener))
}

fun SharedPreferences.onChangeListener(
    listener: (key: String?) -> Unit
) = OnChangeCloseableListener(this, listener)
