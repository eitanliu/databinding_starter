@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.eitanliu.starter.extension

/**
 * 替代三目条件运算
 */
internal class BooleanOperators

inline fun <R> Boolean.then(
    resolve: () -> R
) = if (this) resolve() else null

inline fun <R> Boolean.reject(
    reject: () -> R
) = if (!this) reject() else null

inline fun <R> (() -> Boolean).then(
    resolve: () -> R
) = if (this()) resolve() else null

inline fun <R> (() -> Boolean).reject(
    crossinline reject: () -> R
) = if (!this()) reject() else null

inline fun <R, RES : R, ERR : R> Boolean.thenValue(
    resolve: RES, reject: ERR
) = then(resolve, reject)

inline fun <R, RES : R, ERR : R> (() -> Boolean).thenValue(
    resolve: RES, reject: ERR
) = then(resolve, reject)

inline fun <R, RES : R, ERR : R> Boolean.then(
    resolve: RES, reject: ERR
) = if (this) resolve else reject

inline fun <R, RES : R, ERR : R> (() -> Boolean).then(
    resolve: RES, reject: ERR
) = if (this()) resolve else reject

inline fun <R, RES : R, ERR : R> Boolean.thenInvoke(
    resolve: () -> RES, reject: () -> ERR
) = then(resolve, reject)

inline fun <R, RES : R, ERR : R> (() -> Boolean).thenInvoke(
    resolve: () -> RES, reject: () -> ERR
) = then(resolve, reject)

inline fun <R, RES : R, ERR : R> Boolean.then(
    resolve: () -> RES, reject: () -> ERR
) = if (this) resolve() else reject()

inline fun <R, RES : R, ERR : R> (() -> Boolean).then(
    resolve: () -> RES, reject: () -> ERR
) = if (this()) resolve() else reject()

inline fun <T, R> T.withThen(
    boolean: Boolean,
    resolve: (T) -> R
) = if (boolean) resolve(this) else null

inline fun <T, R> T.withThen(
    predicate: () -> Boolean,
    resolve: (T) -> R
) = if (predicate()) resolve(this) else null

inline fun <T, R> T.withThen(
    predicate: (T) -> Boolean,
    resolve: (T) -> R
) = if (predicate(this)) resolve(this) else null

inline fun <T, R> T.withReject(
    boolean: Boolean,
    reject: (T) -> R
) = if (!boolean) reject(this) else null

inline fun <T, R> T.withReject(
    predicate: () -> Boolean,
    reject: (T) -> R
) = if (!predicate()) reject(this) else null

inline fun <T, R> T.withReject(
    predicate: (T) -> Boolean,
    reject: (T) -> R
) = if (!predicate(this)) reject(this) else null

inline fun <R, RES : R, ERR : R> Any?.withThen(
    boolean: Boolean, resolve: RES, reject: ERR
) = if (boolean) resolve else reject

inline fun <T, R, RES : R, ERR : R> Any?.withThen(
    predicate: () -> Boolean, resolve: RES, reject: ERR
) = if (predicate()) resolve else reject

inline fun <T, R, RES : R, ERR : R> T.withThen(
    predicate: (T) -> Boolean, resolve: RES, reject: ERR
) = if (predicate(this)) resolve else reject

inline fun <T, R, RES : R, ERR : R> T.withThen(
    boolean: Boolean, resolve: (T) -> RES, reject: (T) -> ERR
) = if (boolean) resolve(this) else reject(this)

inline fun <T, R, RES : R, ERR : R> T.withThen(
    predicate: () -> Boolean, resolve: (T) -> RES, reject: (T) -> ERR
) = if (predicate()) resolve(this) else reject(this)

inline fun <T, R, RES : R, ERR : R> T.withThen(
    predicate: (T) -> Boolean, resolve: (T) -> RES, reject: (T) -> ERR
) = if (predicate(this)) resolve(this) else reject(this)

inline infix fun (() -> Boolean).and(
    crossinline predicate: () -> Boolean
): () -> Boolean = { this() && predicate() }

inline infix fun (() -> Boolean).or(
    crossinline predicate: () -> Boolean
): () -> Boolean = { this() || predicate() }
