package com.eitanliu.binding.internal;

public interface JvmValue<T> {
    T getValue();

    void setValue(T value);

    T get();

    void set(T value);
}
