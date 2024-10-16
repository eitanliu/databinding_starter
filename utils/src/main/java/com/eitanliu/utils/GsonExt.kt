@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.eitanliu.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.Reader

class GsonExt

inline val <reified T> T.typeToken get() = typeToken<T>()

inline fun <reified T> typeToken() = object : TypeToken<T>() {}

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, typeToken<T>().type)

inline fun <reified T> Gson.fromJson(json: Reader): T = fromJson(json, typeToken<T>().type)

inline fun <reified T> Gson.fromJson(json: JsonReader): T = fromJson(json, typeToken<T>().type)

inline fun <reified T> Gson.fromJson(json: JsonElement): T = fromJson(json, typeToken<T>().type)

