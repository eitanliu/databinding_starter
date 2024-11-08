@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.eitanliu.utils

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.eitanliu.binding.adapter.viewExtController

class ViewExt

@Suppress("UNCHECKED_CAST")
inline fun <T : ViewGroup.LayoutParams> View.asLayoutParams() = layoutParams as T

@Suppress("UNCHECKED_CAST")
inline fun <T : View> View.parentAsView() = parent as T

@Suppress("UNCHECKED_CAST")
inline fun <T : View> View.asView() = this as T

inline val View.index get() = (parent as? ViewGroup)?.indexOfChild(this) ?: -1

/**
 * View可见部分 相对于 自身View位置左上角 的坐标
 */
inline val View.visibleOnSelf get() = Rect().also { getLocalVisibleRect(it) }

/**
 * View可见部分 相对于 父View 的坐标
 */
inline val View.visibleOnParent
    get() = Rect().also {
        getLocalVisibleRect(it)
        val point = pointOnParent
        it.offset(point.x, point.y)
    }

/**
 * View可见部分 相对于 RootView 的坐标
 */
inline val View.visibleOnRoot get() = Rect().also { getGlobalVisibleRect(it) }

/**
 * View可见部分 相对于 Window 的坐标
 */
inline val View.visibleOnWindow
    get() = Rect().also {
        getGlobalVisibleRect(it)
        val point = rootView.pointOnWindow
        it.offset(point.x, point.y)
    }

/**
 * View可见部分 相对于 屏幕 的坐标
 */
inline val View.visibleOnScreen
    get() = Rect().also {
        getGlobalVisibleRect(it)
        val point = rootView.pointOnScreen
        it.offset(point.x, point.y)
    }

/**
 * View可见部分 相对于 RootView 的坐标
 *
 * @param offset Point 偏移量
 * @return Rect
 */
inline fun View.visibleOnRoot(offset: Point) = Rect().also {
    getGlobalVisibleRect(it, offset)
}

/**
 * 获得 View 相对 父View 的坐标
 */
inline val View.rectOnParent
    get() = Rect().also {
        val location = locationOnParent
        it.left = location[0]
        it.top = location[1]
        it.right = it.left + width
        it.bottom = it.top + height
    }

/**
 * 获取控件 相对 窗口Window 的坐标
 */
inline val View.rectOnWindow
    get() = Rect().also {
        val location = locationOnWindow
        it.left = location[0]
        it.top = location[1]
        it.right = it.left + width
        it.bottom = it.top + height
    }

/**
 * 获得 View 相对 屏幕 的绝对坐标
 */
inline val View.rectOnScreen
    get() = Rect().also {
        val location = locationOnScreen
        it.left = location[0]
        it.top = location[1]
        it.right = it.left + width
        it.bottom = it.top + height
    }

inline val View.pointOnParent get() = locationOnParent.let { Point(it[0], it[1]) }

inline val View.pointOnWindow get() = locationOnWindow.let { Point(it[0], it[1]) }

inline val View.pointOnScreen get() = locationOnScreen.let { Point(it[0], it[1]) }

/**
 * 获得 View 相对 父View 的坐标
 */
inline val View.locationOnParent: IntArray
    get() {
        val location = IntArray(2)
        getLocationInWindow(location)
        (parent as? View)?.also { parent ->
            val parentLocation = IntArray(2)
            parent.getLocationInWindow(parentLocation)
            location[0] -= parentLocation[0]
            location[1] -= parentLocation[1]
        }
        return location
    }

/**
 * 获取控件 相对 窗口Window 的坐标
 */
inline val View.locationOnWindow get() = IntArray(2).also { getLocationInWindow(it) }

/**
 * 获得 View 相对 屏幕 的绝对坐标
 */
inline val View.locationOnScreen get() = IntArray(2).also { getLocationOnScreen(it) }

/**
 * 获得 View 相对 Surface 的坐标
 */
inline val View.locationOnSurface
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = IntArray(2).also { getLocationInSurface(it) }

@Suppress("UNCHECKED_CAST")
val View.bindingTags: SparseArray<Any?>
    get() = run {
        getTag(R.id.bindingTags) as? SparseArray<Any?> ?: SparseArray<Any?>().also {
            setTag(R.id.bindingTags, it)
        }
    }

inline fun <reified T> View.getBindingTag(key: Int) = bindingTags.get(key) as? T

inline fun View.setBindingTag(key: Int, tag: Any?) = bindingTags.put(key, tag)

inline val View.layoutInflater: LayoutInflater get() = LayoutInflater.from(context)

inline val View.dividerHeight get() = context.dividerHeight

inline val View.dividerWidth get() = context.dividerWidth

val View.softwareKeyboardController
    get() = viewExtController.softwareKeyboardController

val View.isShowSoftwareKeyboard
    get() = rootWindowInsetsCompat?.isShowSoftwareKeyboard ?: false

inline fun View.toggleSoftKeyboard() {
    if (isShowSoftwareKeyboard) {
        hideSoftKeyboard()
    } else {
        showSoftKeyboard()
    }
}

inline fun View.hideSoftKeyboard() {
    softwareKeyboardController.hide()
}

inline fun View.showSoftKeyboard() {
    softwareKeyboardController.show()
}

inline val View.systemBarsInsets
    get() = rootWindowInsetsCompat?.systemBarsInsets

inline val View.statusBarsInsets
    get() = rootWindowInsetsCompat?.statusBarsInsets

inline val View.navigationBarsInsets
    get() = rootWindowInsetsCompat?.navigationBarsInsets

inline val View.imeInsets
    get() = rootWindowInsetsCompat?.imeInsets

val View.rootWindowInsetsCompat
    get() = viewExtController.rootWindowInsetsCompat

var View.cacheWindowInsetsCompat
    get() = viewExtController.cacheWindowInsetsCompat
    set(value) {
        viewExtController.cacheWindowInsetsCompat = value
    }

var View.viewWindowInsetsCompat
    get() = viewExtController.viewWindowInsetsCompat
    set(value) {
        viewExtController.viewWindowInsetsCompat = value
    }
