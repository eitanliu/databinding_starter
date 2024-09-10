package com.eitanliu.starter.utils

import com.eitanliu.starter.binding.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * 反射工具类
 */
object ReflectionUtil {

    /**
     * 获取ViewModel的类型
     */
    fun getViewModelGenericType(any: Any): Class<*> {
        val type = if (any is Class<*>) {
            any.genericSuperclass
        } else {
            any.javaClass.genericSuperclass
        }
        return if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<*>
        } else {
            BaseViewModel::class.java
        }
    }

}