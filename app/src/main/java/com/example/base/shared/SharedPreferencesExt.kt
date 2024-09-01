package com.example.base.shared

import android.content.SharedPreferences
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.gson.reflect.TypeToken

class SharedPreferencesExt

@Suppress("NOTHING_TO_INLINE")
inline fun <T> SharedPreferences.property(
    default: T, key: String? = null
) = SharedProperty(default, this, key)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> SharedPreferences.propertyOrNull(
    default: T? = null, key: String? = null
) = property(default, key)

inline fun <reified T> SharedPreferences.jsonProperty(
    default: T, key: String? = null,
    noinline onUpdate: ((value: T) -> Unit)? = null
) = SharedJsonProperty(default, object : TypeToken<T>() {}, this, onUpdate, key)

inline fun <reified T> SharedPreferences.jsonPropertyOrNull(
    default: T? = null, key: String? = null,
    noinline onUpdate: ((value: T?) -> Unit)? = null
) = jsonProperty(default, key, onUpdate)

fun SharedPreferences.onChangeListener(owner: LifecycleOwner, listener: (key: String?) -> Unit) {

    owner.lifecycle.addObserver(object : SharedPreferences.OnSharedPreferenceChangeListener,
        DefaultLifecycleObserver {
        init {
            registerOnSharedPreferenceChangeListener(this)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            unregisterOnSharedPreferenceChangeListener(this)
            owner.lifecycle.removeObserver(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
            listener(key)
        }
    })
}