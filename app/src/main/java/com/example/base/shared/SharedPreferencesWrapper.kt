@file:Suppress("DEPRECATION", "unused")

package com.example.base.shared

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences

private val IGNORE_PREFIX = arrayOf(
    "WebView",
)

// 根据名字判断是否使用系统SharedPreferences
internal val defaultIgnorePredicate = block@{ name: String? ->
    if (name.isNullOrEmpty()) return@block false
    IGNORE_PREFIX.any { name.startsWith(it) }
}

/**
 * MMKV 替换系统 SharedPreferences
 * @receiver Context
 * @param name String?
 * @param mode Int
 * @param ignorePredicate 使用系统SharedPreferences条件
 * @return SharedPreferences
 */
@JvmOverloads
fun Context.wrapperPreferences(
    name: String? = null,
    @PreferencesMode mode: Int = PreferencesMode.MODE_PRIVATE,
    cryptKey: String? = null,
    ignorePredicate: (name: String?) -> Boolean = defaultIgnorePredicate
): SharedPreferences {

    val preferences = when {
        ignorePredicate(name) -> getBasePreferences(name, mode)
        else -> MMKVWrapper(name, mode, cryptKey)
    }
    return preferences
}

// 获取系统 SharedPreferences
fun Context.getBasePreferences(name: String?, mode: Int): SharedPreferences {
    return when (this) {
        is ContextWrapper -> baseContext.getBasePreferences(name, mode)
        else -> getSharedPreferences(name, mode)
    }
}

// 获取未代理 SharedPreferences
fun SharedPreferences.getOriginPreferences(): SharedPreferences {
    return when (this) {
        is SharedPreferencesDelegate -> base.getOriginPreferences()
        else -> this
    }
}

inline fun <reified T : SharedPreferences> SharedPreferences.typeOf() =
    takeIf { it is T } as? T

fun SharedPreferences.takeIf(predicate: (preferences: SharedPreferences) -> Boolean): SharedPreferences? {
    if (predicate(this)) {
        return this
    } else if (this is SharedPreferencesDelegate) {
        base.takeIf(predicate)
    }
    return null
}

open class SharedPreferencesWrapper(
    override val base: SharedPreferences
) : SharedPreferencesDelegate, SharedPreferences by base {

    override fun edit(): SharedPreferences.Editor {
        return Editor(base.edit())
    }

    open class Editor(
        editor: SharedPreferences.Editor
    ) : SharedPreferences.Editor by editor
}

