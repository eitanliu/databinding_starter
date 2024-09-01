@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.example.base.extension

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.view.SoftwareKeyboardControllerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.base.R

class ViewExt

@Suppress("UNCHECKED_CAST")
inline fun <T : ViewGroup.LayoutParams> View.asLayoutParams() = layoutParams as T

@Suppress("UNCHECKED_CAST")
inline fun <T : View> View.parentAsView() = parent as T

@Suppress("UNCHECKED_CAST")
inline fun <T : View> View.asView() = this as T

inline fun View.setBindingTag(key: Int, tag: Any?) = bindingTags.put(key, tag)

inline fun View.getBindingTag(key: Int) = bindingTags.get(key)

@Suppress("UNCHECKED_CAST")
val View.bindingTags: SparseArray<Any?>
    get() = run {
        getTag(R.id.bindingTags) as? SparseArray<Any?> ?: SparseArray<Any?>().also {
            setTag(R.id.bindingTags, it)
        }
    }

inline val View.index get() = (parent as? ViewGroup)?.indexOfChild(this) ?: -1

/**
 * 获得 View 相对 父View 的坐标
 */
inline val View.frameOnParent
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
inline val View.frameOnWindow
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
inline val View.frameOnScreen
    get() = Rect().also {
        val location = locationOnScreen
        it.left = location[0]
        it.top = location[1]
        it.right = it.left + width
        it.bottom = it.top + height
    }

/**
 * 获得 View 相对 父View 的坐标
 */
inline val View.locationOnParent: IntArray
    get() {
        val location = IntArray(2)
        getLocationInWindow(location)
        (parent as View?)?.also { parent ->
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

/**
 * View可见部分 相对于 屏幕的坐标
 */
inline val View.visibleOnScreen get() = Rect().also { getGlobalVisibleRect(it) }

/**
 * View可见部分 相对于 屏幕的坐标
 *
 * @param offset Point 偏移量
 * @return Rect
 */
inline fun View.visibleOnScreen(offset: Point) = Rect().also { getGlobalVisibleRect(it, offset) }

/**
 * View可见部分 相对于 自身View位置左上角的坐标
 */
inline val View.visibleOnSelf get() = Rect().also { getLocalVisibleRect(it) }

/**
 * 获取LifecycleOwner
 */
fun View.findLifecycleOwner(): LifecycleOwner? {
    val binding = DataBindingUtil.findBinding<ViewDataBinding>(this)
    var lifecycleOwner = binding?.lifecycleOwner
    if (lifecycleOwner == null) {
        lifecycleOwner = context.takeIf { it is LifecycleOwner } as? LifecycleOwner
    }
    return lifecycleOwner
}

val View.softwareKeyboardController
    get() = getBindingTag(R.id.softwareKeyboardController) as? SoftwareKeyboardControllerCompat
        ?: SoftwareKeyboardControllerCompat(this).also {
            setBindingTag(R.id.softwareKeyboardController, it)
        }

inline val View.systemBarsInsets
    get() = rootWindowInsetsCompat?.systemBarsInsets

inline val View.statusBarsInsets
    get() = rootWindowInsetsCompat?.statusBarsInsets

inline val View.navigationBarsInsets
    get() = rootWindowInsetsCompat?.navigationBarsInsets

val View.rootWindowInsetsCompat
    get() = ViewCompat.getRootWindowInsets(this)

fun View.getWindowInsetsController(
    window: Window
) = getBindingTag(R.id.windowInsetsController) as? WindowInsetsControllerCompat
    ?: WindowInsetsControllerCompat(window, this).also {
        setBindingTag(R.id.windowInsetsController, it)
    }
