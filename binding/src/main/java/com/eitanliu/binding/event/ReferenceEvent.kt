@file:Suppress("unused")

package com.eitanliu.binding.event

import com.eitanliu.utils.emptyWeakReference
import com.eitanliu.utils.refWeak
import java.lang.ref.Reference

interface ReferenceEvent

interface ReferenceValue<T> : ReferenceEvent {
    val refValue: Reference<T>
}

interface ReferenceResult<T> : ReferenceEvent {
    val refResult: Reference<T>
}

interface ReferenceEventResult<R> : UiEventResult<R>, ReferenceResult<R>

interface ReferenceConsumer<T> : UiEventConsumer<T>, ReferenceValue<T>

interface ReferenceConsumerResult<T, R> : UiEventConsumerResult<T, R>, ReferenceValue<T>

interface ReferenceConsumerAndResult<T, R> : UiEventConsumerResult<T, R>,
    ReferenceValue<T>, ReferenceResult<R>

inline fun <reified R> weakEventResult(
    event: UiEventResult<R>,
): ReferenceEventResult<R> =
    object : ReferenceEventResult<R> {

        private var weakResult: Reference<R> = emptyWeakReference()

        override val refResult: Reference<R> get() = weakResult

        override fun invoke(): R = run {
            event().also { weakResult = it.refWeak() }
        }
    }

inline fun <reified T> weakConsumer(
    consumer: UiEventConsumer<T>,
): ReferenceConsumer<T> =
    object : ReferenceConsumer<T> {
        private var weak: Reference<T> = emptyWeakReference()

        override val refValue: Reference<T> get() = weak

        override fun invoke(value: T) {
            weak = value.refWeak()
            consumer(value)
        }
    }

inline fun <reified T, reified R> weakConsumerResult(
    consumer: UiEventConsumerResult<T, R>,
): ReferenceConsumerResult<T, R> =
    object : ReferenceConsumerResult<T, R> {
        private var weak: Reference<T> = emptyWeakReference()
        override val refValue: Reference<T> get() = weak
        override fun invoke(value: T): R = run {
            weak = value.refWeak()
            consumer(value)
        }
    }

inline fun <reified T, reified R> weakConsumerAndResult(
    consumer: UiEventConsumerResult<T, R>,
): ReferenceConsumerAndResult<T, R> =
    object : ReferenceConsumerAndResult<T, R> {
        private var weak: Reference<T> = emptyWeakReference()

        private var weakResult: Reference<R> = emptyWeakReference()

        override val refValue: Reference<T> get() = weak

        override val refResult: Reference<R> get() = weakResult

        override fun invoke(value: T): R = run {
            weak = value.refWeak()
            consumer(value).also { weakResult = it.refWeak() }
        }
    }