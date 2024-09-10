package com.eitanliu.starter.extension

import java.util.Stack

class StackExt

fun <T> Stack<T>.popOrNull() = isNotEmpty().then { pop() }