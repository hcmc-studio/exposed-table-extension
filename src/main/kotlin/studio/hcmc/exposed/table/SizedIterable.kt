package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.SizedIterable
import studio.hcmc.kotlin.protocol.SortOrder
import studio.hcmc.kotlin.protocol.toExposedSortOrder

fun <T> SizedIterable<T>.orderBy(vararg order: Pair<Expression<*>, SortOrder>): SizedIterable<T> {
    return orderBy(*Array(order.size) { i ->
        val (expression, sortOrder) = order[i]
        expression to sortOrder.toExposedSortOrder()
    })
}

fun <T> SizedIterable<T>.page(pageSize: Int, pageIndex: Int?): SizedIterable<T> {
    if (pageIndex == null) {
        return this
    } else {
        return limit(pageSize, (pageIndex * pageSize).toLong())
    }
}