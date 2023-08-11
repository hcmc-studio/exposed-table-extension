package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder

fun <T : Comparable<T>, S : T?> SortOrder.buildOp(expression: ExpressionWithColumnType<in S>, value: T): Op<Boolean> {
    return when (this) {
        SortOrder.ASC,
        SortOrder.ASC_NULLS_FIRST,
        SortOrder.ASC_NULLS_LAST -> Op.build { expression greater value }
        SortOrder.DESC,
        SortOrder.DESC_NULLS_FIRST,
        SortOrder.DESC_NULLS_LAST -> Op.build { expression less value }
    }
}