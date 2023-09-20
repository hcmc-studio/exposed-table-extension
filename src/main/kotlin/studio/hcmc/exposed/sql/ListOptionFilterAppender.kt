package studio.hcmc.exposed.sql

import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op

fun <T : Comparable> Op<Boolean>.append(
    expr: ExpressionWithColumnType<T>,
    eq: T? = null,
    neq: T? = null,
    less: T? = null,
    lessEq: T? = null,
    greater: T? = null,
    greaterEq: T? = null,
    like: T? = null,
    notLike: T? = null,
    inList: Iterable<T>? = null,
    notInList: Iterable<T>? = null
): Op<Boolean> {
    return this
        .andEq(eq, expr)
        .andNeq(neq, expr)
        .andLess(less, expr)
        .andLessEq(lessEq, expr)
}