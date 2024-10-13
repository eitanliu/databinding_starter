package com.eitanliu.utils

import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

class ReferenceExt

val emptyWeakReference = WeakReference(null)

val emptySoftReference = SoftReference(null)

inline fun <reified T> emptyWeakReference() = WeakReference<T>(null)

inline fun <reified T> emptySoftReference() = SoftReference<T>(null)

inline fun <reified T> T.refWeak() = WeakReference(this)

inline fun <reified T> T.refSoft() = SoftReference(this)