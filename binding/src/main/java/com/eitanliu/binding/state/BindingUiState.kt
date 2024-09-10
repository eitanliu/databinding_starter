@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.binding.state

class BindingUiState


/**
 * 状态只消费一次
 * 通常用在 Activity 或 Fragment 接收消息执行其它操作
 */
inline fun <T> singleState() = singleLiveState<T>()

inline fun <T> singleState(value: T) = singleLiveState(value)

inline fun <T> lateSingleState() = lateSingleLiveState<T>()

/**
 * 状态可消费多次
 * 通常用在布局数据绑定
 */
inline fun <T> multipleState() = multipleLiveState<T>()

inline fun <T> multipleState(value: T) = multipleLiveState(value)

inline fun <T> lateMultipleState() = lateMultipleLiveState<T>()

