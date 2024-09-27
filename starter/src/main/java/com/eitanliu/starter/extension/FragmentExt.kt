@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.starter.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.eitanliu.binding.extension.createFragment
import com.eitanliu.binding.extension.findOrCreateFragment
import com.eitanliu.starter.bundle.BundleDelegate

class FragmentExt


inline fun <reified T : Fragment> FragmentManager.findOrCreateFragment(
    args: BundleDelegate?, tag: String? = null
) = findOrCreateFragment(T::class.java, args?.bundle, tag)

@JvmOverloads
inline fun <T : Fragment> FragmentManager.findOrCreateFragment(
    clazz: Class<T>, args: BundleDelegate?, tag: String? = null
) = findOrCreateFragment(clazz, args?.bundle, tag)

inline fun <reified T : Fragment> createFragment(
    args: BundleDelegate? = null
): T = createFragment(args?.bundle)

inline fun <T : Fragment> Class<T>.createFragment(
    args: BundleDelegate? = null
): T = createFragment(args?.bundle)