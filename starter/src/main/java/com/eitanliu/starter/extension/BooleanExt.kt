@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.starter.extension

class BooleanExt

inline fun Boolean?.not(default: Boolean = false) = (this ?: default).not()