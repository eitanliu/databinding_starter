package com.example.app.binding

import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.childFragmentManager
import com.eitanliu.binding.R
import com.eitanliu.binding.extension.bindingFragment
import com.eitanliu.binding.extension.bindingFragmentActivity
import com.eitanliu.binding.extension.findFragment
import com.eitanliu.binding.extension.getBindingTag
import com.eitanliu.binding.extension.setBindingTag
import com.eitanliu.binding.extension.show
import com.example.app.binding.model.FragmentItem
import com.google.android.material.navigation.NavigationBarView
import kotlin.math.min


/**
 * [com.google.android.material.R.styleable.BottomNavigationView]
 *  [com.google.android.material.R.attr.bottomNavigationStyle]
 *  [com.google.android.material.R.style.Widget_Material3_BottomNavigationView]
 */
class NavigationBarViewAdapter

var NavigationBarView.selectedItemIndex
    get() = getMenuItemIndex(menuItemSelected)
    set(value) {
        val item = menu.getItem(value)
        selectedItemId = item.itemId

    }

var NavigationBarView.menuItemSelected
    get() = getBindingTag(R.id.menuItemSelected) as? MenuItem
    private set(value) {
        setBindingTag(R.id.menuItemSelected, value)
    }

fun NavigationBarView.getMenuItemIndex(item: MenuItem?) = menu.run {
    item ?: return@run 0
    for (index in 0 until size()) {
        if (getItem(index).itemId == item.itemId) return@run index
    }
    0
}

@InverseBindingAdapter(
    attribute = "selectedItemIndex",
    event = "selectedAttrChanged"
)
fun NavigationBarView.bindSelectedItemIndex(): Int {
    return selectedItemIndex
}

@BindingAdapter(
    "items",
    "itemsContainer",
    "selectedItemIndex",
    "selectedAttrChanged",
    requireAll = false,
)
fun NavigationBarView.setItems(
    items: List<FragmentItem>?,
    @IdRes containerViewId: Int,
    index: Int? = null,
    selectedAttrChanged: InverseBindingListener? = null,
) {
    val owner = bindingFragment ?: bindingFragmentActivity
    val fragmentManager = owner?.childFragmentManager ?: return
    if (!items.isNullOrEmpty()) {
        val i = index ?: 0
        if (index != null && i != selectedItemIndex) {
            selectedItemIndex = index
        }
        val item = items[min(i, items.size - 1)]
        val tag = item.tag ?: item.clazz.name
        // val fragment = fragmentManager.findOrCreateFragment(item.clazz, item.args, item.tag)
        val fragment = fragmentManager.findFragment(item.clazz, tag) ?: item.create()
        fragmentManager.show(containerViewId, fragment, tag)
            .commitAllowingStateLoss()
    }

    setOnItemSelectedListener { item ->
        val i = getMenuItemIndex(item)
        val isEnable = items?.get(i)?.isEnabled ?: true
        if (isEnable) {
            menuItemSelected = item
            selectedAttrChanged?.onChange()
        }
        isEnable
    }
}