@file:Suppress("NOTHING_TO_INLINE")

package com.example.binding.event

class BindingEvent

/**
 * 无参事件
 */
inline fun bindingEvent(noinline event: UiEvent) = event

/**
 * 无参返回值事件
 */
inline fun <R> bindingEventResult(noinline event: UiEventResult<R>) = event

/**
 * 带参事件
 */
inline fun <T> bindingConsumer(noinline consumer: UiEventConsumer<T>) = consumer

/**
 * 带参返回值事件
 */
inline fun <T, R> bindingConsumerResult(noinline consumer: UiEventConsumerResult<T, R>) = consumer
