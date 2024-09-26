package com.eitanliu.starter.extension

import com.eitanliu.binding.extension.then
import java.util.Stack

class StackExt

fun <T> Stack<T>.popOrNull() = isNotEmpty().then { pop() }