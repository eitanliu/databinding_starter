@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.example.binding.state

class BindingLiveState

/**
 * 状态只消费一次
 */
inline fun <T> singleState() = SingleLiveState<T?>()

inline fun <T> singleState(value: T) = SingleLiveState(value)

/**
 * 状态可消费多次
 */
inline fun <T> multipleState() = MultipleLiveState<T?>()

inline fun <T> multipleState(value: T) = MultipleLiveState(value)