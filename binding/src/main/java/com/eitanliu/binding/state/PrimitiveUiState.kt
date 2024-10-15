package com.eitanliu.binding.state

// class PrimitiveUiState

typealias SingleUiState<T> = SingleLiveState<T>
typealias SingleUiStateBoolean = SingleUiState<Boolean>
typealias SingleUiStateByte = SingleUiState<Byte>
typealias SingleUiStateChart = SingleUiState<Char>
typealias SingleUiStateShort = SingleUiState<Short>
typealias SingleUiStateInt = SingleUiState<Int>
typealias SingleUiStateLong = SingleUiState<Long>
typealias SingleUiStateFloat = SingleUiState<Float>
typealias SingleUiStateDouble = SingleUiState<Double>
typealias SingleUiStateEnum<T> = SingleUiState<T>

typealias MultipleUiState<T> = MultipleLiveState<T>
typealias MultipleUiStateBoolean = MultipleUiState<Boolean>
typealias MultipleUiStateByte = MultipleUiState<Byte>
typealias MultipleUiStateChart = MultipleUiState<Char>
typealias MultipleUiStateShort = MultipleUiState<Short>
typealias MultipleUiStateInt = MultipleUiState<Int>
typealias MultipleUiStateLong = MultipleUiState<Long>
typealias MultipleUiStateFloat = MultipleUiState<Float>
typealias MultipleUiStateDouble = MultipleUiState<Double>
typealias MultipleUiStateEnum<T> = MultipleLiveState<T>