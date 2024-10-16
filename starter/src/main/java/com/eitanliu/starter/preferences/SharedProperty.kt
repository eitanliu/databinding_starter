package com.eitanliu.starter.preferences

import android.content.SharedPreferences
import com.eitanliu.serializer.StringSerializer
import kotlin.reflect.KProperty

@Suppress("MemberVisibilityCanBePrivate")
open class SharedProperty<T>(
    val default: T,
    prefs: SharedPreferences,
    val key: String? = null,
    val serializer: StringSerializer<T> = StringSerializer.None(),
    val onUpdate: ((value: T) -> Unit)? = null,
) {
    private val prefs = prefs.typeOf<SafetyPreferences>() ?: prefs

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getValue(key ?: property.name, default)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putValue(key ?: property.name, value)


    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    protected open fun getValue(name: String, default: T): T = with(prefs) {
        if (default == null && !prefs.contains(name)) return@with null as T

        val type = serializer.rawType
        val res = when (type) {
            java.lang.Integer::class.java -> getInt(name, default as? Int ?: 0)
            java.lang.Long::class.java -> getLong(name, default as? Long ?: 0)
            java.lang.Float::class.java -> getFloat(name, default as? Float ?: 0f)
            java.lang.Double::class.java -> {
                val def = default as? Double ?: 0.0
                if (this is SafetyPreferences) {
                    getDouble(name, def)
                } else {
                    val value = getLong(name, def.toRawBits())
                    Double.fromBits(value)
                }
            }

            java.lang.Boolean::class.java -> getBoolean(name, default as? Boolean ?: false)
            java.lang.String::class.java -> getString(name, default as? String)
            else -> serializer.decode(getString(name, default as? String))
        } as T
        return res
    }

    protected open fun putValue(name: String, value: T) = with(prefs.edit()) {

        when (value) {
            is Int -> putInt(name, value)
            is Long -> putLong(name, value)
            is Float -> putFloat(name, value)
            is Double -> {
                if (this is SafetyPreferences.Editor) {
                    putDouble(name, value)
                } else {
                    putLong(name, value.toRawBits())
                }
            }

            is Boolean -> putBoolean(name, value)
            is String -> putString(name, value)
            null -> remove(name)
            else -> putString(name, serializer.encode(value))
        }
        onUpdate?.invoke(value)
        apply()
    }
}
