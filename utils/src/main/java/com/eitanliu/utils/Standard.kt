@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.utils


inline fun hashCodeArray(vararg array: Any?, initial: Int = 1): Int {
    return array.fold(initial) { acc, obj ->
        31 * acc + obj.hashCode()
    }
}