package com.example.base.extension

import java.util.*

class StackExt

fun <T> Stack<T>.popOrNull() = isNotEmpty().then({ pop() }, { null })