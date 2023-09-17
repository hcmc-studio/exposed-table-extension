package studio.hcmc.exposed.sql

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.bitwiseAnd
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap
import studio.hcmc.kotlin.protocol.BitMask
import studio.hcmc.kotlin.protocol.BitMaskFlag

infix fun ExpressionWithColumnType<in LocalDate>.dateEq(t: LocalDate): EqOp {
    return EqOp(this, wrap(t))
}

infix fun ExpressionWithColumnType<in LocalDateTime>.dateEq(t: LocalDateTime): Between {
    val localTimeZero = LocalTime(0, 0, 0, 0)
    val from = LocalDateTime(t.date, localTimeZero)
    val to = LocalDateTime(t.date, localTimeZero)

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