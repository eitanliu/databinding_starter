package com.eitanliu.starter.preferences

import android.content.SharedPreferences
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class OnChangeLifecycleListener(
    private val pref: SharedPreferences, val listener: (key: String?) -> Unit
) : SharedPreferences.OnSharedPreferenceChangeListener, DefaultLifecycleObserver {

    init {
        pref.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        pref.unregisterOnSharedPreferenceChangeListener(this)
        owner.lifecycle.removeObserver(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        listener(key)
    }

}