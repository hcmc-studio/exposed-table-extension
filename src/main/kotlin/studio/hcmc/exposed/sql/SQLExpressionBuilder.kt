package studio.hcmc.exposed.sql

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.bitwiseAnd
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap
import org.jetbrains.exposed.sql.vendors.H2Dialect
import org.jetbrains.exposed.sql.vendors.OracleDialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import studio.hcmc.exposed.table.castToExpressionTypeForH2BitWiseIps
import studio.hcmc.kotlin.format.zero
import studio.hcmc.kotlin.protocol.BitMask
import studio.hcmc.kotlin.protocol.BitMaskFlag

infix fun ExpressionWithColumnType<in LocalDate>.dateEq(t: LocalDate): EqOp {
    return EqOp(this, wrap(t))
}

infix fun ExpressionWithColumnType<in LocalDateTime>.dateEq(t: LocalDateTime): Between {
    val from = LocalDateTime(t.date, LocalTime.zero)
    val to = LocalDateTime(t.date, LocalTime.zero)

    return Between(this, wrap(from), wrap(to))
}

infix fun ExpressionWithColumnType<in Instant>.dateEq(t: Instant): Between {
    val localTimeZero = LocalTime(0, 0, 0, 0)
    val localDateTime = t.toLocalDateTime(TimeZone.UTC)
    val from = LocalDateTime(localDateTime.date, localTimeZero).toInstant(TimeZone.UTC)
    val to = LocalDateTime(localDateTime.date.plus(1, DateTimeUnit.DAY), localTimeZero).toInstant(TimeZone.UTC)

    return Between(this, wrap(from), wrap(to))
}

infix fun <E> ExpressionWithColumnType<in BitMask<E>>.enabled(flag: E): Op<Boolean> where E : BitMaskFlag, E : Enum<E> {
    return (this bitwiseAnd BitMask(flag)) neq BitMask()
}

infix fun <E> ExpressionWithColumnType<in BitMask<E>>.disabled(flag: E): Op<Boolean> where E : BitMaskFlag, E : Enum<E> {

    return (this bitwiseAnd BitMask(flag)) eq BitMask()
}

infix fun ExpressionWithColumnType<BitMask<*>>.enabled(value: Int) : Op<Boolean> {
    return AndBitMaskOp<Number, BitMask<*>, Int>(this, wrap(value), columnType) eq value
}

infix fun ExpressionWithColumnType<BitMask<*>>.disabled(value: Int) : Op<Boolean> {
    return AndBitMaskOp<Number, BitMask<*>, Int>(this, wrap(value), columnType) neq value
}

private class AndBitMaskOp<T, S1 : T, S2 : T>(
    /** The left-hand side operand. */
    val expr1: Expression<S1>,
    /** The right-hand side operand. */
    val expr2: Expression<S2>,
    /** The column type of this expression. */
    override val columnType: IColumnType
) : ExpressionWithColumnType<T>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = queryBuilder {
        when (val dialect = currentDialect) {
            is OracleDialect -> append("BITAND(", expr1, ", ", expr2, ")")
            is H2Dialect -> {
                when (dialect.isSecondVersion) {
                    false -> append("BITAND(", expr1, ", ", expr2, ")")
                    true -> {
                        + "BITAND("
                        castToExpressionTypeForH2BitWiseIps(expr1, this)
                        + ", "
                        castToExpressionTypeForH2BitWiseIps(expr2, this)
                        + ")"
                    }
                }
            }
            else -> append('(', expr1, " & ", expr2, ')')
        }
    }
}