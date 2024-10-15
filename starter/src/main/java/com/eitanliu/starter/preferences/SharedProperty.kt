package com.eitanliu.starter.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KProperty

@Suppress("MemberVisibilityCanBePrivate")
open class SharedProperty<T>(
    val default: T,
    prefs: SharedPreferences,
    val key: String? = null
) {
    private val prefs = prefs.typeOf<SafetyPreferences>() ?: prefs

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getValue(key ?: property.name, default)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putValue(key ?: property.name, value)


    @Suppress("UNCHECKED_CAST")
    protected open fun getValue(name: String, default: T): T = with(prefs) {
        if (default == null && !prefs.contains(name)) return@with null as T

        val res = when (default) {
            is Int -> getInt(name, default)
            is Long -> getLong(name, default)
            is Float -> getFloat(name, default)
            is Double -> {
                if (this is SafetyPreferences) {
                    getDouble(name, default)
                } else {
                    val value = getLong(name, default.toRawBits())
                    Double.fromBits(value)
                }
            }

            is Boolean -> getBoolean(name, default)
            is String -> getString(name, default)
            is String? -> getString(name, default)
            else -> throw IllegalArgumentException()
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
            else -> throw IllegalArgumentException()
        }
        apply()
    }
}

@Suppress("MemberVisibilityCanBePrivate")
open class SharedJsonProperty<T>(
    val default: T,
    val typeToken: TypeToken<T>,
    prefs: SharedPreferences,
    val onUpdate: ((value: T) -> Unit)? = null,
    val key: String? = null
) {
    private val prefs = prefs.typeOf<SafetyPreferences>() ?: prefs

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getValue(key ?: property.name)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putValue(key ?: property.name, value)

    protected open fun getValue(name: String): T = with(prefs) {
        // val default = if (List::class.java.isAssignableFrom(typeToken.rawType)) "[]" else "{}"
        val json = getString(name, null) ?: return@with default
        return Gson().fromJson(json, typeToken.type)
    }

    protected open fun putValue(name: String, value: T) = with(prefs.edit()) {
        onUpdate?.invoke(value)
        val json = value?.let { Gson().toJson(it) }
        putString(name, json)
        apply()
    }
}