package com.eitanliu.binding.event

typealias UiEvent = () -> Unit

typealias UiEventResult<R> = () -> R

typealias UiEventConsumer<T> = (T) -> Unit

typealias UiEventConsumerResult<T, R> = (T) -> R