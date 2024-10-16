package com.eitanliu.serializer

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface StringSerializer<T> : TypeSerializer<T, String> {

    class None<T>(
        override val rawType: Class<in T> = object : TypeToken<T>() {}.rawType
    ) : StringSerializer<T> {
        override fun decode(string: String?): T? {
            throw IllegalArgumentException()
        }

        override fun encode(value: T?): String? {
            throw IllegalArgumentException()
        }
    }

    class Json<T>(
        val typeToken: TypeToken<T> = object : TypeToken<T>() {},
    ) : StringSerializer<T> {
        override val rawType: Class<in T> = typeToken.rawType

        override fun decode(string: String?): T? {
            return Gson().fromJson(string, typeToken)
        }

        override fun encode(value: T?): String? {
            if (value == null) return null
            return Gson().toJson(value)
        }
    }
}
