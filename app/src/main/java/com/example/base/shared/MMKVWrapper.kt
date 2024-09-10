@file:Suppress("DEPRECATION", "unused")

package com.example.base.shared

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.base.shared.SafetyPreferences.Companion.parserKeyType
import com.example.base.shared.SafetyPreferences.Companion.toKeyType
import com.example.base.shared.SafetyPreferences.Type
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVContentChangeNotification

open class MMKVWrapper @JvmOverloads constructor(
    private val name: String? = null,
    @PreferencesMode private val mode: Int = PreferencesMode.MODE_PRIVATE,
    private val cryptKey: String? = null,
    private val rootPath: String? = null,
    override val base: MMKV = name?.run {
        MMKV.mmkvWithID(name, mode.modeMMKV, cryptKey, rootPath)
    } ?: MMKV.defaultMMKV(mode.modeMMKV, cryptKey)
) : SharedPreferencesDelegate, SafetyPreferences, SafetyPreferences.Editor {

    companion object {
        const val DEFAULT_NAME = "mmkv.default"

        private val mmkvChangeNotifyListeners = hashSetOf<MMKVContentChangeNotification>()
        private val mmkvChangeNotify = MMKVContentChangeNotification {
            mmkvChangeNotifyListeners.forEach { notify ->
                notify.onContentChangedByOuterProcess(it)
            }
        }

        fun onChangeListener(owner: LifecycleOwner, listener: MMKVContentChangeNotification) {

            owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                init {
                    MMKV.registerContentChangeNotify(mmkvChangeNotify)
                    mmkvChangeNotifyListeners.add(listener)
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    mmkvChangeNotifyListeners.remove(listener)
                }
            })
        }
    }

    private val listeners = hashSetOf<SharedPreferences.OnSharedPreferenceChangeListener>()

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        if (listener == null) return
        listeners.add(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        if (listener == null) return
        listeners.remove(listener)
    }

    private fun notifyListeners(key: String = "") {
        listeners.forEach {
            it.onSharedPreferenceChanged(this, key)
        }
    }

    override fun getAll(): MutableMap<String, *> {
        return hashMapOf<String, Any?>().apply {
            base.allKeys()?.forEach { keyType ->
                val (key, type) = keyType.parserKeyType()
                put(key, getValueForType(key, type))
            }
        }
    }

    private fun getValueForType(key: String, @Type type: String): Any? {
        val keyType = key.toKeyType(type)
        return when (type) {
            Type.BOOLEAN -> base.getBoolean(keyType, false)
            Type.INT -> base.getInt(keyType, 0)
            Type.LONG -> base.getLong(keyType, 0)
            Type.FLOAT -> base.getFloat(keyType, 0f)
            Type.DOUBLE -> getDouble(key, 0.0)
            Type.STRING -> base.getString(keyType, null)
            Type.STRING_SET -> base.getStringSet(keyType, null)
            Type.BYTE_ARRAY -> base.getBytes(keyType, null)
            else -> null
        }
    }

    override fun getString(key: String, defValue: String?): String? {
        return base.getString(key.toKeyType(Type.STRING), defValue)
    }

    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? {
        return base.getStringSet(key.toKeyType(Type.STRING_SET), defValues)
    }

    override fun getInt(key: String, defValue: Int): Int {
        return base.getInt(key.toKeyType(Type.INT), defValue)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return base.getLong(key.toKeyType(Type.LONG), defValue)
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return base.getFloat(key.toKeyType(Type.FLOAT), defValue)
    }

    override fun getDouble(key: String, defValue: Double): Double {
        return if (base.contains(key.toKeyType(Type.DOUBLE))) {
            base.decodeDouble(key.toKeyType(Type.DOUBLE), defValue)
        } else {
            defValue
        }
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return base.getBoolean(key.toKeyType(Type.BOOLEAN), defValue)
    }

    override fun getBytes(key: String, defValue: ByteArray?): ByteArray? {
        return base.getBytes(key.toKeyType(Type.INT), defValue)
    }

    override fun contains(key: String): Boolean {
        for (s in allKeyType(key)) {
            if (base.contains(s)) return true
        }
        return false
    }

    override fun containsForType(key: String?, @Type type: String): Boolean {
        return key?.run { base.contains(toKeyType(type)) } ?: false
    }

    override fun edit(): SafetyPreferences.Editor = this

    override fun putBytes(key: String, value: ByteArray?): SafetyPreferences.Editor {
        base.putBytes(key.toKeyType(Type.BYTE_ARRAY), value)
        notifyListeners(key)
        return this
    }

    override fun putString(key: String, value: String?): SafetyPreferences.Editor {
        base.putString(key.toKeyType(Type.STRING), value)
        notifyListeners(key)
        return this
    }

    override fun putStringSet(
        key: String,
        values: MutableSet<String>?
    ): SafetyPreferences.Editor {
        base.putStringSet(key.toKeyType(Type.STRING_SET), values)
        notifyListeners(key)
        return this
    }

    override fun putInt(key: String, value: Int): SafetyPreferences.Editor {
        base.putInt(key.toKeyType(Type.INT), value)
        notifyListeners(key)
        return this
    }

    override fun putLong(key: String, value: Long): SafetyPreferences.Editor {
        base.putLong(key.toKeyType(Type.LONG), value)
        notifyListeners(key)
        return this
    }

    override fun putFloat(key: String, value: Float): SafetyPreferences.Editor {
        base.putFloat(key.toKeyType(Type.FLOAT), value)
        notifyListeners(key)
        return this
    }

    override fun putDouble(key: String, value: Double): SafetyPreferences.Editor {
        base.encode(key.toKeyType(Type.DOUBLE), value)
        notifyListeners(key)
        return this
    }

    override fun putBoolean(key: String, value: Boolean): SafetyPreferences.Editor {
        base.putBoolean(key.toKeyType(Type.BOOLEAN), value)
        notifyListeners(key)
        return this
    }

    override fun remove(key: String): SafetyPreferences.Editor {
        base.removeValuesForKeys(allKeyType(key).toList().toTypedArray())
        notifyListeners(key)
        return this
    }

    override fun removeForType(key: String, @Type type: String): SafetyPreferences.Editor {
        base.remove(key.toKeyType(type))
        notifyListeners(key)
        return this
    }

    override fun clear(): SafetyPreferences.Editor {
        base.clear()
        notifyListeners()
        return this
    }

    override fun commit(): Boolean {
        return base.commit()
    }

    override fun apply() {
        return base.apply()
    }

}

private val Int.modeMMKV
    get() = if (this == Context.MODE_MULTI_PROCESS) MMKV.MULTI_PROCESS_MODE
    else MMKV.SINGLE_PROCESS_MODE
