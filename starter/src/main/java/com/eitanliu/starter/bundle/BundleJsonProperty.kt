package com.eitanliu.starter.bundle

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KProperty

@Suppress("MemberVisibilityCanBePrivate")
open class BundleJsonProperty<T>(
    val default: T,
    val typeToken: TypeToken<T>,
    val bundle: Bundle,
    val key: String? = null,
    val onUpdate: ((T) -> Unit)? = null,
) {

    open operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        getValue(key ?: property.name)

    open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        putValue(key ?: property.name, value)

    protected fun getValue(name: String): T = with(bundle) {
        // val default = if (List::class.java.isAssignableFrom(typeToken.rawType)) "[]" else "{}"
        val json = getString(name, null) ?: return@with default
        return Gson().fromJson(json, typeToken.type)
    }

    protected fun putValue(name: String, value: T) = with(bundle) {
        onUpdate?.invoke(value)
        val json = Gson().toJson(value)
        putString(name, json)
    }
}