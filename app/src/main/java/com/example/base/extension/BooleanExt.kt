@file:Suppress("NOTHING_TO_INLINE")

package com.example.base.extension

class BooleanExt

inline fun Boolean?.not(default: Boolean = false) = (this ?: default).not()