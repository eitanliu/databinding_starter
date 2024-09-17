@file:Suppress("PublicApiImplicitType", "KDocMissingDocumentation", "unused")

package com.eitanliu.starter.extension

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableByte
import androidx.databinding.ObservableChar
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.databinding.ObservableShort
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ViewModelExt

class ViewModelFactory(
    val create: (() -> ViewModel)? = null
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        create?.invoke() as? T ?: super.create(modelClass)
}

class AndroidViewModelFactory constructor(
    application: Application,
    val create: (() -> ViewModel)? = null
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        create?.invoke() as? T ?: super.create(modelClass)
}


class InjectViewModelFactory<VM : ViewModel> @Inject constructor(
    val viewModel: VM,
) : AbstractSavedStateViewModelFactory() {
    private var created: (InjectViewModelFactory<VM>.(VM) -> Unit)? = null

    fun onCreate(created: (InjectViewModelFactory<VM>.(VM) -> Unit)) = apply {
        this.created = created
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        (viewModel as? T ?: super.create(modelClass)).also {
            created?.invoke(this, it as VM)
        }

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        TODO("Not yet implemented")
    }
}

class InjectAndroidViewModelFactory<VM : ViewModel> @Inject constructor(
    application: Application,
    val viewModel: VM
) : ViewModelProvider.AndroidViewModelFactory(application) {
    private var created: (InjectAndroidViewModelFactory<VM>.(VM) -> Unit)? = null

    fun onCreate(created: (InjectAndroidViewModelFactory<VM>.(VM) -> Unit)) = apply {
        this.created = {
            if (it == viewModel) created.invoke(this, it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        (viewModel as? T ?: super.create(modelClass)).also {
            created?.invoke(this, it as VM)
        }
}

inline val <T : ViewModel> T.selfViewModel get() = this

inline var <T> ObservableField<T>.value
    get() = get()
    set(value) = set(value)

inline var ObservableBoolean.value
    get() = get()
    set(value) = set(value)

inline var ObservableByte.value
    get() = get()
    set(value) = set(value)

inline var ObservableChar.value
    get() = get()
    set(value) = set(value)

inline var ObservableShort.value
    get() = get()
    set(value) = set(value)

inline var ObservableInt.value
    get() = get()
    set(value) = set(value)

inline var ObservableLong.value
    get() = get()
    set(value) = set(value)

inline var ObservableFloat.value
    get() = get()
    set(value) = set(value)

inline var ObservableDouble.value
    get() = get()
    set(value) = set(value)