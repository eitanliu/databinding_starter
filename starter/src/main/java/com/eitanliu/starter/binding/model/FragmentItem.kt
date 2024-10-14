package com.eitanliu.starter.binding.model

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.eitanliu.binding.annotation.ResourcesId
import com.eitanliu.binding.extension.createFragment
import java.io.Serializable

class FragmentItem private constructor(
    val clazz: Class<out Fragment>,
    val args: Bundle?,
    val tag: String? = null,
    @IdRes val id: Int = ResourcesId.ID_NULL,
    private val create: ((FragmentItem) -> Fragment),
) : Serializable {

    companion object {

        inline fun <reified T : Fragment> create(
            crossinline create: (FragmentItem) -> T
        ) = create(null, create)

        inline fun <reified T : Fragment> create(
            args: Bundle?, crossinline create: (FragmentItem) -> T
        ) = create(args, null, create = create)

        inline fun <reified T : Fragment> create(
            args: Bundle?, tag: String? = null, id: Int = ResourcesId.ID_NULL,
            crossinline create: (FragmentItem) -> T
        ) = create(T::class.java, args, tag, id) {
            create.invoke(it)
        }

        inline fun <reified T : Fragment> create(
            args: Bundle? = null, tag: String? = null, id: Int = ResourcesId.ID_NULL,
        ) = create(T::class.java, args, tag, id)

        fun create(
            clazz: Class<out Fragment>, args: Bundle? = null,
            tag: String? = null, id: Int = ResourcesId.ID_NULL,
        ) = create(clazz, args, tag, id) {
            it.clazz.createFragment(args)
        }

        fun create(
            clazz: Class<out Fragment>, args: Bundle? = null,
            tag: String? = null, id: Int = ResourcesId.ID_NULL,
            create: ((FragmentItem) -> Fragment),
        ) = FragmentItem(clazz, args, tag, id, create)

    }

    fun create(): Fragment {
        return create.invoke(this)
    }

    var isEnabled = true
}