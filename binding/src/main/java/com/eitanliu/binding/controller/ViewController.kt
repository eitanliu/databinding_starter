package com.eitanliu.binding.controller

import android.view.View

/**
 * reference [kotlin.coroutines.CoroutineContext]
 */
interface ViewController {

    val key: Key<*>

    val view: View

    operator fun <E : ViewController> get(key: Key<E>): E? =
        @Suppress("UNCHECKED_CAST")
        if (this.key == key) this as E else null

    fun <R> fold(initial: R, operation: (R, ViewController) -> R): R =
        operation(initial, this)

    operator fun plus(controller: ViewController): ViewController =
        if (controller === Empty || controller === this) this else
            controller.fold(this) { acc, element ->
                val removed = acc.minus(element)
                if (removed === Empty) element else Combined(removed, element)
            }

    operator fun minus(controller: ViewController): ViewController {
        return minusKey(controller.key)
    }

    fun minusKey(key: Key<*>): ViewController =
        if (this.key == key) Empty else this

    interface Key<E : ViewController>

    class Combined(
        private val left: ViewController,
        private val element: ViewController,
    ) : ViewController {
        companion object Key : ViewController.Key<Combined>

        override val key: ViewController.Key<*> = Key

        override val view: View get() = element.view

        override fun <E : ViewController> get(key: ViewController.Key<E>): E? {
            var cur = this
            while (true) {
                cur.element[key]?.let { return it }
                val next = cur.left
                if (next is Combined) {
                    cur = next
                } else {
                    return next[key]
                }
            }
        }

        override fun <R> fold(initial: R, operation: (R, ViewController) -> R): R =
            operation(left.fold(initial, operation), element)

        override fun minusKey(key: ViewController.Key<*>): ViewController {
            element[key]?.let { return left }
            val newLeft = left.minusKey(key)
            return when {
                newLeft === left -> this
                newLeft === Empty -> element
                else -> Combined(newLeft, element)
            }
        }
    }

    object Empty : ViewController, Key<Empty> {

        override val key: Key<*> = Empty

        override val view: View get() = throw UnsupportedOperationException("Unsupported View")
    }
}

