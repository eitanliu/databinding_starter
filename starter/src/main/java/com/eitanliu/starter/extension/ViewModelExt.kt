@file:Suppress("PublicApiImplicitType", "KDocMissingDocumentation", "unused")

package com.eitanliu.starter.extension

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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

inline val <T : ViewModel> T.selfViewModel get() = this