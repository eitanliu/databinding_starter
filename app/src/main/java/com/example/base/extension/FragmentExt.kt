package com.example.base.extension

import android.os.Bundle
import androidx.fragment.app.Fragment

class FragmentExt

inline val <T : Fragment> T.selfFragment get() = this


inline fun <reified T : Fragment> newFragment(bundle: Bundle? = null) =
    T::class.java.getConstructor().newInstance().apply {
        arguments = bundle
    }

@Suppress("NOTHING_TO_INLINE")
inline fun <T : Fragment> Class<T>.newFragment(bundle: Bundle? = null) =
    getConstructor().newInstance().apply {
        arguments = bundle
    }