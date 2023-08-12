package studio.hcmc.exposed.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap

typealias ExposedSortOrder = SortOrder

fun <T : Comparable<T>, S : T?> ExposedSortOrder.buildOp(expression: ExpressionWithColumnType<in S>, value: T): ComparisonOp {
    return when (this) {
        ExposedSortOrder.ASC,
        ExposedSortOrder.ASC_NULLS_FIRST,
        ExposedSortOrder.ASC_NULLS_LAST -> GreaterOp(expression, expression.wrap(value))
        ExposedSortOrder.DESC,
        ExposedSortOrder.DESC_NULLS_FIRST,
        ExposedSortOrder.DESC_NULLS_LAST -> LessOp(expression, expression.wrap(value))
    }
}

fun <T : Comparable<T>, S : T?> ExposedSortOrder.buildOp(expression: Expression<in S>, other: Expression<in S>): ComparisonOp {
    return when (this) {
        ExposedSortOrder.ASC,
        ExposedSortOrder.ASC_NULLS_FIRST,
        ExposedSortOrder.ASC_NULLS_LAST -> GreaterOp(expression, other)
        ExposedSortOrder.DESC,
        ExposedSortOrder.DESC_NULLS_FIRST,
        ExposedSortOrder.DESC_NULLS_LAST -> LessOp(expression, other)
    }
}

@JvmName("buildOpEntityId")
fun <T : Comparable<T>> ExposedSortOrder.buildOp(expression: ExpressionWithColumnType<EntityID<T>>, value: T): ComparisonOp {
    return when (this) {
        ExposedSortOrder.ASC,
        ExposedSortOrder.ASC_NULLS_FIRST,
        ExposedSortOrder.ASC_NULLS_LAST -> GreaterOp(expression, expression.wrap(value))
        ExposedSortOrder.DESC,
        ExposedSortOrder.DESC_NULLS_FIRST,
        ExposedSortOrder.DESC_NULLS_LAST -> LessOp(expression, expression.wrap(value))
    }
}