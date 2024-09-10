package com.example.base.extension

import java.util.Stack

class StackExt

fun <T> Stack<T>.popOrNull() = isNotEmpty().then { pop() }