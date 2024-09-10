@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.eitanliu.starter.extension

import android.os.Bundle
import com.eitanliu.starter.bundle.BundleDelegate
import com.eitanliu.starter.bundle.newBundleDelegate
import com.eitanliu.starter.utils.BundleUtils

class BundleExt

@Suppress("HasPlatformType")
inline fun <reified R : BundleDelegate> Bundle.toBundleDelegate() =
    newBundleDelegate<R>(this)

inline fun Bundle?.orEmpty() = this ?: Bundle()

inline fun Bundle.putObject(key: String, value: Any?) = BundleUtils.putObject(this, key, value)
