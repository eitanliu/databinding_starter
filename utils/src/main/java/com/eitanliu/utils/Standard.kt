@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.utils


inline fun hashCodeArray(vararg array: Any?): Int {
    return array.fold(0) { acc, obj ->
        31 * acc + obj.hashCode()
    }
}