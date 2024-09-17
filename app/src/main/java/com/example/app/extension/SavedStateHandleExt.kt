package com.example.app.extension

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.eitanliu.starter.extension.putObject

class SavedStateHandleExt

val SavedStateHandle.bundle: Bundle
    get() {
        val keys = keys()
        return Bundle(keys.size).also {
            keys.forEach { key ->
                it.putObject(key, get(key))
            }
        }
    }