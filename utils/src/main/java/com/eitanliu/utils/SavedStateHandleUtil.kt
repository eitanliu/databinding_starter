package com.eitanliu.utils

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.eitanliu.utils.BundleUtils.putObject

object SavedStateHandleUtil {

    val SavedStateHandle.bundle: Bundle
        get() {
            val keys = keys()
            return Bundle(keys.size).also {
                keys.forEach { key ->
                    it.putObject(key, get(key))
                }
            }
        }

    fun SavedStateHandle.toMap(): Map<String, Any?> {
        val state: MutableMap<String, Any?> = HashMap()
        for (key in keys()) {
            state[key] = this[key]
        }
        return state
    }
}