package com.eitanliu.binding.state

typealias SingleLiveStateBoolean = PrimitiveSingleLiveState<Boolean>
typealias SingleLiveStateByte = PrimitiveSingleLiveState<Byte>
typealias SingleLiveStateChart = PrimitiveSingleLiveState<Char>
typealias SingleLiveStateShort = PrimitiveSingleLiveState<Short>
typealias SingleLiveStateInt = PrimitiveSingleLiveState<Int>
typealias SingleLiveStateLong = PrimitiveSingleLiveState<Long>
typealias SingleLiveStateFloat = PrimitiveSingleLiveState<Float>
typealias SingleLiveStateDouble = PrimitiveSingleLiveState<Double>
typealias SingleLiveStateEnum<T> = PrimitiveSingleLiveState<T>

typealias MultipleLiveStateBoolean = PrimitiveMultipleLiveState<Boolean>
typealias MultipleLiveStateByte = PrimitiveMultipleLiveState<Byte>
typealias MultipleLiveStateChart = PrimitiveMultipleLiveState<Char>
typealias MultipleLiveStateShort = PrimitiveMultipleLiveState<Short>
typealias MultipleLiveStateInt = PrimitiveMultipleLiveState<Int>
typealias MultipleLiveStateLong = PrimitiveMultipleLiveState<Long>
typealias MultipleLiveStateFloat = PrimitiveMultipleLiveState<Float>
typealias MultipleLiveStateDouble = PrimitiveMultipleLiveState<Double>
typealias MultipleLiveStateEnum<T> = PrimitiveMultipleLiveState<T>

class PrimitiveSingleLiveState<T> : SingleLiveState<T> {
    constructor(value: T) : super(value)

    constructor() : super()

    override fun postValue(value: T) {
        when (value) {
            is Boolean, Byte, Char, Short, Int, Float, Long, Float, Double, Enum -> {
                if (value != getValue()) super.postValue(value)
            }

            else -> super.postValue(value)
        }
    }

    override fun setValue(value: T) {
        when (value) {
            is Boolean, Byte, Char, Short, Int, Float, Long, Float, Double, Enum -> {
                if (value != getValue()) super.setValue(value)
            }

            else -> super.setValue(value)
        }
    }
}

class PrimitiveMultipleLiveState<T> : MultipleLiveState<T> {
    constructor(value: T) : super(value)

    constructor() : super()

    override fun postValue(value: T) {
        when (value) {
            is Boolean, Byte, Char, Short, Int, Float, Long, Float, Double, Enum -> {
                if (value != getValue()) super.postValue(value)
            }

            else -> super.postValue(value)
        }
    }

    override fun setValue(value: T) {
        when (value) {
            is Boolean, Byte, Char, Short, Int, Float, Long, Float, Double, Enum -> {
                if (value != getValue()) super.setValue(value)
            }

            else -> super.setValue(value)
        }
    }
}