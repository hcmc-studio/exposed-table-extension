package studio.hcmc.exposed.sql

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.bitwiseAnd
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap
import studio.hcmc.kotlin.protocol.BitMask

class AndBitMaskOp<T : BitMask<*>>(
    val expr1: Expression<T>,
    val expr2: Expression<Int>,
    override val columnType: IColumnType
) : ExpressionWithColumnType<T>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = (
            expr1.castTo<Int>(IntegerColumnType()) bitwiseAnd expr2.castTo(IntegerColumnType())
    ).toQueryBuilder(queryBuilder)
}

class EqBitMaskOp<T : BitMask<*>>(
    expr1: Expression<T>,
    expr2: Expression<Int>
) : ComparisonOp(expr1, expr2, "=")

@Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")
infix fun <T : BitMask<*>> ExpressionWithColumnType<T>.eq(other: Int): EqBitMaskOp<T> {
    return EqBitMaskOp(this, wrap(other))
}

class NeqBitMaskOp<T : BitMask<*>>(
    expr1: Expression<T>,
    expr2: Expression<Int>
) : ComparisonOp(expr1, expr2, "<>")

@Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")
infix fun <T : BitMask<*>> ExpressionWithColumnType<T>.neq(other: Int): NeqBitMaskOp<T> {
    return NeqBitMaskOp(this, wrap(other))
}