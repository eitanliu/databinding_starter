@file:Suppress("unused")

package com.example.base.extension

class AnyExt

inline fun <reified T> Any.asType() = this as T

inline fun <reified T> Any?.asTypeOrNull() = this as? T
