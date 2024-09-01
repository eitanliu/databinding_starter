@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.example.base.extension

import android.os.Bundle
import com.example.base.bundle.BundleDelegate
import com.example.base.bundle.newBundleDelegate
import com.example.base.utils.BundleUtils

class BundleExt

@Suppress("HasPlatformType")
inline fun <reified R : BundleDelegate> Bundle.toBundleDelegate() =
    newBundleDelegate<R>(this)

inline fun Bundle?.orEmpty() = this ?: Bundle()

inline fun Bundle.putObject(key: String, value: Any?) = BundleUtils.putObject(this, key, value)
