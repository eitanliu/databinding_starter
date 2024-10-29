package com.eitanliu.starter.bundle

import android.os.Bundle
import com.eitanliu.utils.BundleUtils.putObject
import kotlin.reflect.KProperty

open class BundleDelegateProperty<T : BundleDelegate>(
    val default: T,
    val bundle: Bundle,
    val key: String? = null,
)

inline operator fun <reified T : BundleDelegate> BundleDelegateProperty<T>.getValue(
    thisRef: Any?, property: KProperty<*>
): T = with(bundle) {
    val name = key ?: property.name
    return getBundle(name)?.let { newBundleDelegate(it) } ?: default
}

inline operator fun <reified T : BundleDelegate> BundleDelegateProperty<T>.setValue(
    thisRef: Any?, property: KProperty<*>, value: T
) = with(bundle) {
    val name = key ?: property.name
    putObject(name, value.bundle)
}