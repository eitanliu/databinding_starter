package com.eitanliu.binding.state

// class PrimitiveUiState

typealias SingleUiState<T> = SingleLiveState<T>
typealias PrimitiveSingleUiState<T> = PrimitiveSingleLiveState<T>
typealias SingleUiStateBoolean = PrimitiveSingleUiState<Boolean>
typealias SingleUiStateByte = PrimitiveSingleUiState<Byte>
typealias SingleUiStateChart = PrimitiveSingleUiState<Char>
typealias SingleUiStateShort = PrimitiveSingleUiState<Short>
typealias SingleUiStateInt = PrimitiveSingleUiState<Int>
typealias SingleUiStateLong = PrimitiveSingleUiState<Long>
typealias SingleUiStateFloat = PrimitiveSingleUiState<Float>
typealias SingleUiStateDouble = PrimitiveSingleUiState<Double>
typealias SingleUiStateEnum<T> = PrimitiveSingleUiState<T>

typealias MultipleUiState<T> = MultipleLiveState<T>
typealias PrimitiveMultipleUiState<T> = PrimitiveMultipleLiveState<T>
typealias MultipleUiStateBoolean = PrimitiveMultipleUiState<Boolean>
typealias MultipleUiStateByte = PrimitiveMultipleUiState<Byte>
typealias MultipleUiStateChart = PrimitiveMultipleUiState<Char>
typealias MultipleUiStateShort = PrimitiveMultipleUiState<Short>
typealias MultipleUiStateInt = PrimitiveMultipleUiState<Int>
typealias MultipleUiStateLong = PrimitiveMultipleUiState<Long>
typealias MultipleUiStateFloat = PrimitiveMultipleUiState<Float>
typealias MultipleUiStateDouble = PrimitiveMultipleUiState<Double>
typealias MultipleUiStateEnum<T> = PrimitiveMultipleUiState<T>