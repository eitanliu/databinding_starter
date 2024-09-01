package com.example.base.extension

import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

class ReferenceExt

val emptyWeakReference = WeakReference(null)

val emptySoftReference = SoftReference(null)

@Suppress("UNCHECKED_CAST")
inline fun <reified T> emptyWeakReference() = emptyWeakReference as WeakReference<T>

@Suppress("UNCHECKED_CAST")
inline fun <reified T> emptySoftReference() = emptySoftReference as SoftReference<T>

inline fun <reified T> T.refWeak() = WeakReference(this)

inline fun <reified T> T.refSoft() = SoftReference(this)