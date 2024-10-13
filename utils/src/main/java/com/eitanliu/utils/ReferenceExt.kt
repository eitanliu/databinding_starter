package com.eitanliu.utils

import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

class ReferenceExt

val emptyWeakReference = WeakReference(null)

val emptySoftReference = SoftReference(null)

inline fun <reified T> weakReference(referent: T? = null) = WeakReference<T>(referent)

inline fun <reified T> softReference(referent: T? = null) = SoftReference<T>(referent)

inline fun <reified T> T.refWeak() = WeakReference(this)

inline fun <reified T> T.refSoft() = SoftReference(this)