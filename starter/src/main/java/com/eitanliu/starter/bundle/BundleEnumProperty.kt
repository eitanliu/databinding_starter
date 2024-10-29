package com.eitanliu.starter.bundle

import android.os.Bundle
import com.eitanliu.utils.BundleUtils.putObject
import kotlin.reflect.KProperty

open class BundleEnumProperty<T : Enum<T>>(
    val default: T,
    val bundle: Bundle,
    val key: String? = null,
)

inline operator fun <reified T : Enum<T>> BundleEnumProperty<T>.getValue(
    thisRef: Any?, property: KProperty<*>
): T = with(bundle) {
    val name = key ?: property.name
    return getString(name)?.let { enumValueOf<T>(it) } ?: default
}

inline operator fun <reified T : Enum<T>> BundleEnumProperty<T>.setValue(
    thisRef: Any?, property: KProperty<*>, value: T
) = with(bundle) {
    val name = key ?: property.name
    putObject(name, value.name)
}