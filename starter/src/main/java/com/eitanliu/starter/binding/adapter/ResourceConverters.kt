@file:Suppress("unused", "ObsoleteSdkInt", "RestrictedApi")

package com.eitanliu.starter.binding.adapter

import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingConversion
import com.eitanliu.starter.utils.ResourceUtil

class ResourceConverters

@BindingConversion
fun convertIdDrawable(@DrawableRes id: Int) = ResourceUtil.getDrawable(id)

@BindingConversion
fun convertIdBoolean(@BoolRes id: Int) = ResourceUtil.getBoolean(id)

@BindingConversion
fun convertIdString(@StringRes id: Int) = ResourceUtil.getString(id)

@BindingConversion
fun convertIdStringArray(@ArrayRes id: Int) = ResourceUtil.getStringArray(id)


