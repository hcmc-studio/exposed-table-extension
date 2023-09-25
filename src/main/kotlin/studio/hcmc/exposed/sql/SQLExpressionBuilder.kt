package studio.hcmc.exposed.sql

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.bitwiseAnd
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap
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

infix fun <T : Instant?> ExpressionWithColumnType<T>.dateEq(t: Instant): Between {
    val localDateTime = t.toLocalDateTime(TimeZone.UTC)
    val from = LocalDateTime(localDateTime.date, LocalTime.zero).toInstant(TimeZone.UTC)
    val to = LocalDateTime(localDateTime.date.plus(1, DateTimeUnit.DAY), LocalTime.zero).toInstant(TimeZone.UTC)

    return Between(this, wrap(from), wrap(to))
}

infix fun <T : Instant?> ExpressionWithColumnType<T>.dateNeq(t: Instant): NotOp<Boolean> {
    return NotOp(this dateEq t)
}

infix fun <E> ExpressionWithColumnType<in BitMask<E>>.enabled(flag: E): Op<Boolean> where E : BitMaskFlag, E : Enum<E> {
    return (this bitwiseAnd BitMask(flag)) neq BitMask()
}

infix fun <E> ExpressionWithColumnType<in BitMask<E>>.disabled(flag: E): Op<Boolean> where E : BitMaskFlag, E : Enum<E> {
    return (this bitwiseAnd BitMask(flag)) eq BitMask()
}

internal infix fun Op<Boolean>.trueAnd(op: Op<Boolean>): Op<Boolean> {
    return if (this == Op.TRUE) {
        op
    } else if (this == Op.FALSE) {
        this
    } else {
        this and op
    }
}

internal infix fun Op<Boolean>.trueOr(op: Op<Boolean>): Op<Boolean> {
    return if (this == Op.TRUE) {
        this
    } else if (this == Op.FALSE) {
        op
    } else {
        this or op
    }
}