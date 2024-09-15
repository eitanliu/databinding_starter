@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.binding.event

class BindingEvent

/**
 * 无参事件
 */
inline fun bindingEvent(
    crossinline event: () -> Unit
): UiEvent = UiEvent {
    event()
}

/**
 * 无参返回值事件
 */
inline fun <R> bindingEventResult(
    crossinline event: () -> R
): UiEventResult<R> = UiEventResult {
    event()
}

/**
 * 带参事件
 */
inline fun <T> bindingConsumer(
    crossinline consumer: (T) -> Unit
): UiEventConsumer<T> = UiEventConsumer { value ->
    consumer(value)
}

/**
 * 带参返回值事件
 */
inline fun <T, R> bindingConsumerResult(
    crossinline consumer: (T) -> R
): UiEventConsumerResult<T, R> = UiEventConsumerResult { value ->
    consumer(value)
}

