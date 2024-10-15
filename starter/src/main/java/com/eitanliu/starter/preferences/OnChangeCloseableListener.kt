package com.eitanliu.starter.preferences

import android.content.SharedPreferences

class OnChangeCloseableListener(
    private val pref: SharedPreferences, val listener: (key: String?) -> Unit
) : SharedPreferences.OnSharedPreferenceChangeListener, AutoCloseable {

    init {
        pref.registerOnSharedPreferenceChangeListener(this)
    }

    override fun close() {
        pref.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        listener(key)
    }

}