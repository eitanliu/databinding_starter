package com.example.app.binding.model

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.eitanliu.binding.annotation.ResourcesId
import java.io.Serializable

data class FragmentItem(
    val clazz: Class<out Fragment>,
    val args: Bundle?,
    val tag: String? = null,
    @IdRes val id: Int = ResourcesId.ID_NULL
) : Serializable {
    constructor(
        clazz: Class<out Fragment>, tag: String? = null
    ) : this(clazz, null, tag)

    var isEnabled = true
}