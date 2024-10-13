@file:Suppress("unused", "ObsoleteSdkInt", "RestrictedApi")

package com.eitanliu.starter.binding.adapter

import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingConversion
import com.eitanliu.utils.ResourcesUtil

class ResourceConverters

@BindingConversion
fun convertIdDrawable(@DrawableRes id: Int) = ResourcesUtil.getDrawable(id)

@BindingConversion
fun convertIdBoolean(@BoolRes id: Int) = ResourcesUtil.getBoolean(id)

@BindingConversion
fun convertIdString(@StringRes id: Int) = ResourcesUtil.getString(id)

@BindingConversion
fun convertIdStringArray(@ArrayRes id: Int) = ResourcesUtil.getStringArray(id)


