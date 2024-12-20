package com.eitanliu.starter.bundle

import android.os.Bundle
import com.eitanliu.utils.BundleUtils.putObject
import kotlin.reflect.KProperty

open class BundleProperty<T>(val default: T, val bundle: Bundle, val key: String? = null)

@Suppress("DEPRECATION")
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