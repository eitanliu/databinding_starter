@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.eitanliu.binding.state

class BindingLiveState

/**
 * 状态只消费一次
 * 通常用在 Activity 或 Fragment 接收消息执行其它操作
 */
inline fun <T> singleLiveState() = SingleLiveState<T?>()

inline fun <T> singleLiveState(value: T) = SingleLiveState(value)

inline fun <T> lateSingleLiveState() = SingleLiveState<T>()

/**
 * 状态可消费多次
 * 通常用在布局数据绑定
 */
inline fun <T> multipleLiveState() = MultipleLiveState<T?>()

inline fun <T> multipleLiveState(value: T) = MultipleLiveState(value)

inline fun <T> lateMultipleLiveState() = MultipleLiveState<T>()