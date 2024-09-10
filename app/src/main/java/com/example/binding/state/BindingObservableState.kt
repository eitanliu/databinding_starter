@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.example.binding.state

class BindingObservableState

/**
 * 状态只消费一次
 * 通常用在 Activity 或 Fragment 接收消息执行其它操作
 */
inline fun <T> singleObservable() = SingleObservableState<T?>()

inline fun <T> singleObservable(value: T) = SingleObservableState(value)

inline fun <T> lateSingleObservable() = SingleObservableState<T>()

/**
 * 状态可消费多次
 * 通常用在布局数据绑定
 */
inline fun <T> multipleObservable() = MultipleObservableState<T?>()

inline fun <T> multipleObservable(value: T) = MultipleObservableState(value)

inline fun <T> lateMultipleObservable() = MultipleObservableState<T>()