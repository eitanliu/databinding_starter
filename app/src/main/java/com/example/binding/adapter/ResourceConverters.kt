@file:Suppress("unused", "ObsoleteSdkInt", "RestrictedApi")

package com.example.binding.adapter

import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingConversion
import com.example.utils.ResourceUtil

class ResourceConverters

@BindingConversion
fun convertIdColorStateList(@ColorRes id: Int) = ResourceUtil.getColorStateList(id)

@BindingConversion
fun convertIdDrawable(@DrawableRes id: Int) = ResourceUtil.getDrawable(id)

@BindingConversion
fun convertIdBoolean(@BoolRes id: Int) = ResourceUtil.getBoolean(id)

@BindingConversion
fun convertIdString(@StringRes id: Int) = ResourceUtil.getString(id)

@BindingConversion
fun convertIdStringArray(@ArrayRes id: Int) = ResourceUtil.getStringArray(id)


