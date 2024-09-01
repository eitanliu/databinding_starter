package com.example.base.extension

import androidx.fragment.app.Fragment

class FragmentExt

inline val <T : Fragment> T.selfFragment get() = this