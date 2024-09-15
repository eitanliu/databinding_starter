package com.eitanliu.binding.event

fun interface UiEvent : () -> Unit

fun interface UiEventResult<R> : () -> R

fun interface UiEventConsumer<T> : (T) -> Unit

fun interface UiEventConsumerResult<T, R> : (T) -> R
