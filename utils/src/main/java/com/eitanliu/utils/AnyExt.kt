@file:Suppress("unused")

package com.eitanliu.utils

class AnyExt

inline fun <reified T> Any.asType() = this as T

inline fun <reified T> Any?.asTypeOrNull() = this as? T
