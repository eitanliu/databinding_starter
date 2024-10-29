@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.eitanliu.starter.extension

import android.os.Bundle
import com.eitanliu.starter.bundle.BundleDelegate
import com.eitanliu.starter.bundle.newBundleDelegate
import com.eitanliu.utils.BundleUtils.putObject

class BundleExt

@Suppress("HasPlatformType")
inline fun <reified R : BundleDelegate> Bundle.toBundleDelegate() =
    newBundleDelegate<R>(this)

inline fun Bundle?.orEmpty() = this ?: Bundle()

inline operator fun Bundle.set(key: String, value: Any?) = putObject(key, value)
