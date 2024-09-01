package com.example.base.extension

class EnumExt

inline fun <reified T: Enum<T>> enumOrdinalOf(ordinal: Int): T {
    return enumValues<T>()[ordinal]
}