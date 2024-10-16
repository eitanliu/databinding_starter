package com.eitanliu.serializer

interface TypeSerializer<T, R> {
    val rawType: Class<in T>

    fun decode(string: R?): T?

    fun encode(value: T?): R?
}