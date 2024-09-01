@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.example.binding.state

class BindingObservableState

/**
 * 状态只消费一次
 */
inline fun <T> singleObservable() = SingleObservableState<T?>()

inline fun <T> singleObservable(value: T) = SingleObservableState(value)

/**
 * 状态可消费多次
 */
inline fun <T> multipleObservable() = MultipleObservableState<T?>()

inline fun <T> multipleObservable(value: T) = MultipleObservableState(value)