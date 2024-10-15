package com.eitanliu.starter.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface StringSerializer<T> {
    val typeToken: TypeToken<T>

    fun decode(string: String?): T?

    fun encode(value: T?): String?

    class None<T>(
        override val typeToken: TypeToken<T> = object : TypeToken<T>() {}
    ) : StringSerializer<T> {
        override fun decode(string: String?): T? {
            throw IllegalArgumentException()
        }

        override fun encode(value: T?): String? {
            throw IllegalArgumentException()
        }
    }

    class Json<T>(
        override val typeToken: TypeToken<T> = object : TypeToken<T>() {}
    ) : StringSerializer<T> {
        override fun decode(string: String?): T? {
            return Gson().fromJson(string, typeToken.type)
        }

        override fun encode(value: T?): String? {
            if (value == null) return null
            return Gson().toJson(value)
        }
    }
}