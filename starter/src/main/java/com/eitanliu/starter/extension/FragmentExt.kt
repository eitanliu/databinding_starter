@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.starter.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.eitanliu.starter.bundle.BundleDelegate

class FragmentExt

inline val <T : Fragment> T.selfFragment get() = this

inline fun FragmentManager.show(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    tag: String? = null
) = show(beginTransaction(), containerViewId, fragment, tag)

fun FragmentManager.show(
    transaction: FragmentTransaction,
    @IdRes containerViewId: Int,
    fragment: Fragment,
    tag: String? = null
) = with(transaction) {
    val frags = fragments
    frags.filter { it != fragment }.forEach { f ->
        hide(f)
    }
    if (!fragment.isAdded) {
        add(containerViewId, fragment, tag ?: fragment::class.java.canonicalName)
    }
    show(fragment)
}

@Suppress("CAST_NEVER_SUCCEEDS")
inline fun <reified T : Fragment> FragmentManager.findOrCreateFragment(
    tag: String? = null
) = findOrCreateFragment(T::class.java, null as? Bundle, tag)

inline fun <reified T : Fragment> FragmentManager.findOrCreateFragment(
    args: BundleDelegate?, tag: String? = null
) = findOrCreateFragment(T::class.java, args?.bundle, tag)

inline fun <reified T : Fragment> FragmentManager.findOrCreateFragment(
    args: Bundle?, tag: String? = null
) = findOrCreateFragment(T::class.java, args, tag)

@Suppress("CAST_NEVER_SUCCEEDS")
@JvmOverloads
inline fun <T : Fragment> FragmentManager.findOrCreateFragment(
    clazz: Class<T>, tag: String? = null
) = findOrCreateFragment(clazz, null as? Bundle, tag)

@JvmOverloads
inline fun <T : Fragment> FragmentManager.findOrCreateFragment(
    clazz: Class<T>, args: BundleDelegate?, tag: String? = null
) = findOrCreateFragment(clazz, args?.bundle, tag)

@Suppress("UNCHECKED_CAST")
@JvmOverloads
inline fun <T : Fragment> FragmentManager.findOrCreateFragment(
    clazz: Class<T>, args: Bundle?, tag: String? = null
) = findFragmentByTag(tag ?: clazz.canonicalName) as? T ?: clazz.createFragment(args)

@Suppress("CAST_NEVER_SUCCEEDS")
inline fun <reified T : Fragment> createFragment(): T = createFragment(null as? Bundle)

inline fun <reified T : Fragment> createFragment(
    args: BundleDelegate? = null
): T = createFragment(args?.bundle)

inline fun <reified T : Fragment> createFragment(
    args: Bundle? = null
): T = T::class.java.createFragment(args)

inline fun <T : Fragment> Class<T>.createFragment(
    args: BundleDelegate? = null
): T = createFragment(args?.bundle)

inline fun <T : Fragment> Class<T>.createFragment(
    args: Bundle? = null
): T = getConstructor().newInstance().apply {
    arguments = args
}