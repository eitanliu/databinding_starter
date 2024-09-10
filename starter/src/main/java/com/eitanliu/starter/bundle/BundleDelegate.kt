@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.eitanliu.starter.bundle

import android.os.Bundle
import com.eitanliu.starter.extension.typeToken

interface BundleDelegate {
    val bundle: Bundle
}

@Suppress("HasPlatformType")
inline fun <reified T : BundleDelegate, reified R : BundleDelegate> T.toBundleDelegate() =
    newBundleDelegate<R>(this.bundle)

@Suppress("HasPlatformType")
inline fun <reified T : BundleDelegate> newBundleDelegate(bundle: Bundle = Bundle()) =
    T::class.java.getConstructor(Bundle::class.java).newInstance(bundle)

@Suppress("HasPlatformType")
inline fun <T : BundleDelegate> Class<T>.newBundleDelegate(bundle: Bundle = Bundle()) =
    getConstructor(Bundle::class.java).newInstance(bundle)

inline fun <reified T> BundleDelegate.propertyOrNull(
    default: T? = null, key: String? = null
) = property(default, key)

inline fun <reified T> BundleDelegate.property(default: T, key: String? = null) =
    BundleProperty(default, bundle, key)

inline fun <reified T : Enum<T>> BundleDelegate.property(default: T, key: String? = null) =
    BundleEnumProperty(default, bundle, key)

inline fun <reified T : BundleDelegate> BundleDelegate.propertyOrNull(
    default: T? = null, key: String? = null
) = property(default, key)

inline fun <reified T : BundleDelegate> BundleDelegate.property(default: T, key: String? = null) =
    BundleDelegateProperty(default, bundle, key)

inline fun <reified T> BundleDelegate.jsonPropertyOrNull(
    default: T? = null, key: String? = null,
    noinline onUpdate: ((value: T?) -> Unit)? = null
) = jsonProperty(default, key, onUpdate)

inline fun <reified T> BundleDelegate.jsonProperty(
    default: T, key: String? = null,
    noinline onUpdate: ((T) -> Unit)? = null,
) = BundleJsonProperty(default, typeToken(), bundle, key, onUpdate)
