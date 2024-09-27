package com.example.app.binding

import android.view.View
import androidx.databinding.BindingConversion

class Converters

@BindingConversion
fun convertNumberString(number: Number) = number.toString()

@BindingConversion
fun convertFloatString(number: Float) = number.toString()

@BindingConversion
fun convertViewId(view: View) = view.id