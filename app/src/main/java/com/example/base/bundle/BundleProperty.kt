package com.example.base.bundle

import android.os.Bundle
import com.example.base.extension.putObject
import kotlin.reflect.KProperty

open class BundleProperty<T>(val default: T, val bundle: Bundle, val key: String? = null)

inline operator fun <reified T> BundleProperty<T>.getValue(
    thisRef: Any?, property: KProperty<*>,
): T = with(bundle) {
    val name = key ?: property.name
    return get(name) as? T ?: default
}

inline operator fun <reified T> BundleProperty<T>.setValue(
    thisRef: Any?, property: KProperty<*>, value: T,
) = with(bundle) {
    val name = key ?: property.name
    putObject(name, value)
}