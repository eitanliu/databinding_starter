package com.eitanliu.binding.state

// class PrimitiveUiState

typealias SingleUiState<T> = SingleLiveState<T>
typealias PrimitiveSingleUiState<T> = PrimitiveSingleLiveState<T>
typealias SingleUiStateBoolean = PrimitiveSingleLiveState<Boolean>
typealias SingleUiStateByte = PrimitiveSingleLiveState<Byte>
typealias SingleUiStateChart = PrimitiveSingleLiveState<Char>
typealias SingleUiStateShort = PrimitiveSingleLiveState<Short>
typealias SingleUiStateInt = PrimitiveSingleLiveState<Int>
typealias SingleUiStateLong = PrimitiveSingleLiveState<Long>
typealias SingleUiStateFloat = PrimitiveSingleLiveState<Float>
typealias SingleUiStateDouble = PrimitiveSingleLiveState<Double>
typealias SingleUiStateEnum<T> = PrimitiveSingleLiveState<T>

typealias MultipleUiState<T> = MultipleLiveState<T>
typealias PrimitiveMultipleUiState<T> = PrimitiveMultipleLiveState<T>
typealias MultipleUiStateBoolean = PrimitiveMultipleLiveState<Boolean>
typealias MultipleUiStateByte = PrimitiveMultipleLiveState<Byte>
typealias MultipleUiStateChart = PrimitiveMultipleLiveState<Char>
typealias MultipleUiStateShort = PrimitiveMultipleLiveState<Short>
typealias MultipleUiStateInt = PrimitiveMultipleLiveState<Int>
typealias MultipleUiStateLong = PrimitiveMultipleLiveState<Long>
typealias MultipleUiStateFloat = PrimitiveMultipleLiveState<Float>
typealias MultipleUiStateDouble = PrimitiveMultipleLiveState<Double>
typealias MultipleUiStateEnum<T> = PrimitiveMultipleLiveState<T>