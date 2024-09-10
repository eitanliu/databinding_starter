@file:Suppress("unused", "HasPlatformType")

package com.eitanliu.binding.adapter

import android.graphics.drawable.Drawable
import androidx.databinding.BindingConversion
import com.eitanliu.binding.state.UiState

class UiStateConverters

@BindingConversion
fun <T> convertUiState(state: UiState<T>): T = state.value

@BindingConversion
fun convertUiState(state: UiState<Boolean>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Byte>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Char>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Short>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Int>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Long>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Float>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Double>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Enum<*>>) = state.value

@BindingConversion
fun convertUiState(state: UiState<String>) = state.value

@BindingConversion
fun convertUiState(state: UiState<Drawable>) = state.value
