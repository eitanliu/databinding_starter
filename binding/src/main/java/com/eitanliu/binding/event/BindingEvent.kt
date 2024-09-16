@file:Suppress("NOTHING_TO_INLINE")

package com.eitanliu.binding.event

import com.eitanliu.binding.listener.CLICK_DELAY_DEFAULT

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

/**
 * 防抖无参事件
 */
inline fun debounceEvent(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline event: () -> Unit
): UiEvent = object : UiEvent {

    private var lastInvokeTime: Long = 0L

    override fun invoke() {

        val current = System.currentTimeMillis()
        if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            event.invoke()
        }
    }
}

/**
 * 防抖无参事件
 */
inline fun debounceEvent(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline repeat: (() -> Unit),
    crossinline event: () -> Unit
): UiEvent = object : UiEvent {

    private var lastInvokeTime: Long = 0L

    override fun invoke() {

        val current = System.currentTimeMillis()
        if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            event.invoke()
        } else {
            repeat.invoke()
        }
    }
}


/**
 * 防抖无参返回值事件
 */
inline fun <R> debounceEventResult(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline event: () -> R
): UiEventResult<R> = object : UiEventResult<R> {

    private var lastInvokeTime: Long = 0L
    private var temp: R? = null

    @Suppress("UNCHECKED_CAST")
    override fun invoke(): R {
        val current = System.currentTimeMillis()

        if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            temp = event.invoke()
        }
        return temp as R
    }
}

/**
 * 防抖无参返回值事件
 */
inline fun <R> debounceEventResult(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline repeat: () -> R,
    crossinline event: () -> R
): UiEventResult<R> = object : UiEventResult<R> {

    private var lastInvokeTime: Long = 0L

    override fun invoke(): R {
        val current = System.currentTimeMillis()
        return if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            event.invoke()
        } else {
            repeat.invoke()
        }
    }
}

/**
 * 防抖带参事件
 */
inline fun <T> debounceConsumer(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline event: (T) -> Unit
): UiEventConsumer<T> = object : UiEventConsumer<T> {

    private var lastInvokeTime: Long = 0L

    override fun invoke(value: T) {
        val current = System.currentTimeMillis()
        if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            event.invoke(value)
        }
    }
}

/**
 * 防抖带参事件
 */
inline fun <T> debounceConsumer(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline repeat: (T) -> Unit,
    crossinline event: (T) -> Unit
): UiEventConsumer<T> = object : UiEventConsumer<T> {

    private var lastInvokeTime: Long = 0L

    override fun invoke(value: T) {
        val current = System.currentTimeMillis()
        if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            event.invoke(value)
        } else {
            repeat.invoke(value)
        }
    }
}

/**
 * 防抖带参返回值事件
 */
inline fun <T, R> debounceConsumerResult(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline event: (T) -> R
) = object : UiEventConsumerResult<T, R> {

    private var lastInvokeTime: Long = 0L
    private var temp: R? = null

    @Suppress("UNCHECKED_CAST")
    override fun invoke(value: T): R {
        val current = System.currentTimeMillis()
        if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            temp = event.invoke(value)
        }
        return temp as R
    }
}

/**
 * 防抖带参返回值事件
 */
inline fun <T, R> debounceConsumerResult(
    delay: Long = CLICK_DELAY_DEFAULT,
    crossinline repeat: ((T) -> R),
    crossinline event: (T) -> R
) = object : UiEventConsumerResult<T, R> {

    private var lastInvokeTime: Long = 0L

    override fun invoke(value: T): R {
        val current = System.currentTimeMillis()
        return if (current - lastInvokeTime >= delay) {
            lastInvokeTime = current
            event.invoke(value)
        } else {
            repeat.invoke(value)
        }
    }
}

