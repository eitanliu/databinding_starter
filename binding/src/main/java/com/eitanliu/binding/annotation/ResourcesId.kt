package com.eitanliu.binding.annotation

import androidx.core.content.res.ResourcesCompat

@Retention(AnnotationRetention.SOURCE)
annotation class ResourcesId {
    companion object {
        const val ID_NULL = ResourcesCompat.ID_NULL
    }
}