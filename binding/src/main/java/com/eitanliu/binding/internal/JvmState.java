package com.eitanliu.binding.internal;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

public interface JvmState<T> {
    void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer);

    T get();

    void set(T value);

    T getValue();

    void setValue(T value);

    void postValue(T value);

    void invoke(T value);

    void notifyChange();
}
