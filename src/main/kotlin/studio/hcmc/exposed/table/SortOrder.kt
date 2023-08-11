package studio.hcmc.exposed.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap

fun <T : Comparable<T>, S : T?> SortOrder.buildOp(expression: ExpressionWithColumnType<in S>, value: T): ComparisonOp {
    return when (this) {
        SortOrder.ASC,
        SortOrder.ASC_NULLS_FIRST,
        SortOrder.ASC_NULLS_LAST -> GreaterOp(expression, expression.wrap(value))
        SortOrder.DESC,
        SortOrder.DESC_NULLS_FIRST,
        SortOrder.DESC_NULLS_LAST -> LessOp(expression, expression.wrap(value))
    }
}

fun <T : Comparable<T>, S : T?> SortOrder.buildOp(expression: Expression<in S>, other: Expression<in S>): ComparisonOp {
    return when (this) {
        SortOrder.ASC,
        SortOrder.ASC_NULLS_FIRST,
        SortOrder.ASC_NULLS_LAST -> GreaterOp(expression, other)
        SortOrder.DESC,
        SortOrder.DESC_NULLS_FIRST,
        SortOrder.DESC_NULLS_LAST -> LessOp(expression, other)
    }
}

fun <T : Comparable<T>> SortOrder.buildOp(expression: ExpressionWithColumnType<EntityID<T>>, value: T): ComparisonOp {
    return when (this) {
        SortOrder.ASC,
        SortOrder.ASC_NULLS_FIRST,
        SortOrder.ASC_NULLS_LAST -> GreaterOp(expression, expression.wrap(value))
        SortOrder.DESC,
        SortOrder.DESC_NULLS_FIRST,
        SortOrder.DESC_NULLS_LAST -> LessOp(expression, expression.wrap(value))
    }
}