package com.eitanliu.starter.utils

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.eitanliu.starter.binding.BindingViewModel
import java.lang.reflect.ParameterizedType

/**
 * 反射工具类
 */
object ReflectionUtil {

    /**
     * 获取[ViewModel]的类型
     */
    fun getViewModelGenericType(any: Any): Class<out ViewModel> =
        findGenericType(any, ViewModel::class.java) ?: BindingViewModel::class.java

    /**
     * 获取[ViewDataBinding]的类型
     */
    fun getDataBindingGenericType(any: Any): Class<out ViewDataBinding> =
        findGenericType(any, ViewDataBinding::class.java) ?: ViewDataBinding::class.java

    /**
     * 获取[ViewBinding]的类型
     */
    fun getViewBindingGenericType(any: Any): Class<out ViewBinding> =
        findGenericType(any, ViewBinding::class.java) ?: ViewBinding::class.java

    fun <T> findGenericType(any: Any, clazz: Class<T>): Class<T>? {
        val genericType = if (any is Class<*>) {
            any.genericSuperclass
        } else {
            any.javaClass.genericSuperclass
        } as? ParameterizedType
        if (genericType == null) return null
        val outClazz = genericType.actualTypeArguments.firstNotNullOfOrNull { type ->
            when {
                type is Class<*> && clazz.isAssignableFrom(type) -> type
                else -> null
            }
        }
        @Suppress("UNCHECKED_CAST")
        return outClazz as? Class<T>
    }

}