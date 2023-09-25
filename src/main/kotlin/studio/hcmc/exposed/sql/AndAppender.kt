package studio.hcmc.exposed.sql

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eqSubQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.hasFlag
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inSubQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isDistinctFrom
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotDistinctFrom
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.SqlExpressionBuilder.match
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notEqSubQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notInList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notInSubQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notLike
import org.jetbrains.exposed.sql.SqlExpressionBuilder.regexp
import org.jetbrains.exposed.sql.ops.SingleValueInListOp
import org.jetbrains.exposed.sql.vendors.FunctionProvider
import studio.hcmc.kotlin.format.toKotlinInstant
import java.sql.Date
import kotlin.time.Duration.Companion.days

fun <T> Op<Boolean>.andEq(t: T?, expression: ExpressionWithColumnType<T>): Op<Boolean> {
    return t?.let { this trueAnd (expression eq it) } ?: this
}

fun <T> Op<Boolean>.andEq(t: T?, column: CompositeColumn<T>): Op<Boolean> {
    return t?.let { this trueAnd (column eq it) } ?: this
}

fun <T, S1 : T?, S2 : T?> Op<Boolean>.andEq(t: Expression<in S1>?, expression: Expression<in S2>): Op<Boolean> {
    return t?.let { this trueAnd (expression eq it) } ?: this
}

fun <T : Comparable<T>, E : EntityID<T>?, V : T?> Op<Boolean>.andEq(t: V?, expression: ExpressionWithColumnType<E>): Op<Boolean> {
    return t?.let { this trueAnd (expression eq it) } ?: this
}

fun <T> Op<Boolean>.andNeq(t: T?, expression: ExpressionWithColumnType<T>): Op<Boolean> {
    return t?.let { this trueAnd (expression neq it) } ?: this
}

fun <T, S1 : T?, S2 : T?> Op<Boolean>.andNeq(t: Expression<in S1>?, expression: Expression<in S2>): Op<Boolean> {
    return t?.let { this trueAnd (expression neq it) } ?: this
}

fun <T : Comparable<T>, E : EntityID<T>?, V : T?> Op<Boolean>.andNeq(t: V?, expression: ExpressionWithColumnType<E>): Op<Boolean> {
    return t?.let { this trueAnd (expression neq it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andLess(t: T?, expression: ExpressionWithColumnType<S>): Op<Boolean> {
    return t?.let { this trueAnd (expression less it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andLess(t: Expression<in S>?, expression: Expression<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression less it) } ?: this
}

@JvmName("andLessEntityID")
fun <T : Comparable<T>> Op<Boolean>.andLess(t: T?, expression: ExpressionWithColumnType<EntityID<T>>): Op<Boolean> {
    return t?.let { this trueAnd (expression less it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andLessEq(t: T?, expression: ExpressionWithColumnType<S>): Op<Boolean> {
    return t?.let { this trueAnd (expression lessEq it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andLessEq(t: Expression<in S>?, expression: Expression<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression lessEq it) } ?: this
}

@JvmName("andLessEqEntityID")
fun <T : Comparable<T>> Op<Boolean>.andLessEq(t: T?, expression: ExpressionWithColumnType<EntityID<T>>): Op<Boolean> {
    return t?.let { this trueAnd (expression lessEq it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andGreater(t: T?, expression: ExpressionWithColumnType<S>): Op<Boolean> {
    return t?.let { this trueAnd (expression greater it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andGreater(t: Expression<in S>?, expression: Expression<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression greater it) } ?: this
}

@JvmName("andGreaterEntityID")
fun <T : Comparable<T>> Op<Boolean>.andGreater(t: T?, expression: ExpressionWithColumnType<EntityID<T>>): Op<Boolean> {
    return t?.let { this trueAnd (expression greater it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andGreaterEq(t: T?, expression: ExpressionWithColumnType<S>): Op<Boolean> {
    return t?.let { this trueAnd (expression greaterEq it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andGreaterEq(t: Expression<in S>?, expression: Expression<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression greaterEq it) } ?: this
}

@JvmName("andGreaterEqEntityID")
fun <T : Comparable<T>> Op<Boolean>.andGreaterEq(t: T?, expression: ExpressionWithColumnType<EntityID<T>>): Op<Boolean> {
    return t?.let { this trueAnd (expression greaterEq it) } ?: this
}

fun <T, S : T?> Op<Boolean>.andBetween(from: T?, to: T?, expression: ExpressionWithColumnType<S>): Op<Boolean> {
    return from?.let { f -> to?.let { t -> this trueAnd expression.between(f, t) } } ?: this
}

fun <T> Op<Boolean>.andIsNull(t: Any?, expression: Expression<T>): Op<Boolean> {
    return t?.let { this trueAnd expression.isNull() } ?: this
}

fun <T> Op<Boolean>.andIsNotNull(t: Any?, expression: Expression<T>): Op<Boolean> {
    return t?.let { this trueAnd expression.isNotNull() } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andIsNotDistinctFrom(t: T?, expression: ExpressionWithColumnType<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression isNotDistinctFrom it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andIsNotDistinctFrom(t: Expression<in S>?, expression: Expression<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression isNotDistinctFrom it) } ?: this
}

@JvmName("andIsNotDistinctFromEntityID")
fun <T : Comparable<T>> Op<Boolean>.andIsNotDistinctFrom(t: T?, expression: ExpressionWithColumnType<EntityID<T>>): Op<Boolean> {
    return t?.let { this trueAnd (expression isNotDistinctFrom it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andIsDistinctFrom(t: T?, expression: ExpressionWithColumnType<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression isDistinctFrom it) } ?: this
}

fun <T : Comparable<T>, S : T?> Op<Boolean>.andIsDistinctFrom(t: Expression<in S>?, expression: Expression<in S>): Op<Boolean> {
    return t?.let { this trueAnd (expression isDistinctFrom it) } ?: this
}

@JvmName("andIsDistinctFromEntityID")
fun <T : Comparable<T>> Op<Boolean>.andIsDistinctFrom(t: T?, expression: ExpressionWithColumnType<EntityID<T>>): Op<Boolean> {
    return t?.let { this trueAnd (expression isDistinctFrom it) } ?: this
}

fun <T> Op<Boolean>.andHasFlag(t: T?, expression: ExpressionWithColumnType<T>): Op<Boolean> {
    return t?.let { this trueAnd (expression hasFlag it) } ?: this
}

fun <T : String?> Op<Boolean>.andLike(pattern: String?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression like it.wrapLikePattern()) } ?: this
}

fun <T : String?> Op<Boolean>.andLike(pattern: LikePattern?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression like it) } ?: this
}

@JvmName("andLikeWithEntityID")
fun Op<Boolean>.andLike(pattern: String?, expression: Expression<EntityID<String>>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression like it.wrapLikePattern()) } ?: this
}

@JvmName("andLikeWithEntityID")
fun Op<Boolean>.andLike(pattern: LikePattern?, expression: Expression<EntityID<String>>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression like it) } ?: this
}

fun <T : String?> Op<Boolean>.andLike(pattern: ExpressionWithColumnType<String>?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression like it) } ?: this
}

@JvmName("andLikeWithEntityIDAndExpression")
fun Op<Boolean>.andLike(pattern: ExpressionWithColumnType<String>?, expression: Expression<EntityID<String>>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression like it) } ?: this
}

fun <T : String?> Op<Boolean>.andMatch(pattern: String?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression match it) } ?: this
}

fun <T : String?> Op<Boolean>.andMatch(pattern: String?, mode: FunctionProvider.MatchMode?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd expression.match(it, mode) } ?: this
}

fun <T : String?> Op<Boolean>.andNotLike(pattern: String?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression notLike it) } ?: this
}

fun <T : String?> Op<Boolean>.andNotLike(pattern: LikePattern?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression notLike it) } ?: this
}

@JvmName("andNotLikeWithEntityID")
fun Op<Boolean>.andNotLike(pattern: String?, expression: Expression<EntityID<String>>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression notLike it) } ?: this
}

@JvmName("andNotLikeWithEntityID")
fun Op<Boolean>.andNotLike(pattern: LikePattern?, expression: Expression<EntityID<String>>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression notLike it) } ?: this
}

fun <T : String?> Op<Boolean>.andNotLike(pattern: ExpressionWithColumnType<String>?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression notLike it) } ?: this
}

@JvmName("andNotLikeWithEntityIDAndExpression")
fun Op<Boolean>.andNotLike(pattern: ExpressionWithColumnType<String>?, expression: Expression<EntityID<String>>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression notLike it) } ?: this
}

fun <T : String?> Op<Boolean>.andRegexp(pattern: String?, expression: Expression<T>): Op<Boolean> {
    return pattern?.let { this trueAnd (expression regexp it) } ?: this
}

fun <T : String?> Op<Boolean>.andRegexp(pattern: Expression<String>?, expression: Expression<T>, caseSensitive: Boolean = true): Op<Boolean> {
    return pattern?.let { this trueAnd (expression.regexp(it, caseSensitive)) } ?: this
}

fun <T> Op<Boolean>.andInSubQuery(query: AbstractQuery<*>?, expression: Expression<T>): Op<Boolean> {
    return query?.let { this trueAnd (expression inSubQuery it) } ?: this
}

fun <T> Op<Boolean>.andNotInSubQuery(query: AbstractQuery<*>?, expression: Expression<T>): Op<Boolean> {
    return query?.let { this trueAnd (expression notInSubQuery it) } ?: this
}

fun <T> Op<Boolean>.andEqSubQuery(query: AbstractQuery<*>?, expression: Expression<T>): Op<Boolean> {
    return query?.let { this trueAnd (expression eqSubQuery it) } ?: this
}

fun <T> Op<Boolean>.andNotEqSubQuery(query: AbstractQuery<*>?, expression: Expression<T>): Op<Boolean> {
    return query?.let { this trueAnd (expression notEqSubQuery it) } ?: this
}

fun <T> Op<Boolean>.andInList(list: Iterable<T>?, expression: ExpressionWithColumnType<T>): Op<Boolean> {
    return list?.let { this trueAnd (expression inList it) } ?: this
}

fun <T1, T2> Op<Boolean>.andInList(list: Iterable<Pair<T1, T2>>?, expression: Pair<ExpressionWithColumnType<T1>, ExpressionWithColumnType<T2>>): Op<Boolean> {
    return list?.let { this trueAnd (expression inList it) } ?: this
}

fun <T1, T2, T3> Op<Boolean>.andInList(list: Iterable<Triple<T1, T2, T3>>?, expression: Triple<ExpressionWithColumnType<T1>, ExpressionWithColumnType<T2>, ExpressionWithColumnType<T3>>): Op<Boolean> {
    return list?.let { this trueAnd (expression inList it) } ?: this
}

@JvmName("andInListIds")
fun <T : Comparable<T>, ID : EntityID<T>?> Op<Boolean>.andInList(list: Iterable<T>?, column: Column<ID>): Op<Boolean> {
    return list?.let { this trueAnd (column inList it) } ?: this
}

fun <T> Op<Boolean>.andNotInList(list: Iterable<T>?, expression: ExpressionWithColumnType<T>): Op<Boolean> {
    return list?.let { this trueAnd (expression notInList it) } ?: this
}

fun <T1, T2> Op<Boolean>.andNotInList(list: Iterable<Pair<T1, T2>>?, expression: Pair<ExpressionWithColumnType<T1>, ExpressionWithColumnType<T2>>): Op<Boolean> {
    return list?.let { this trueAnd (expression notInList it) } ?: this
}

fun <T1, T2, T3> Op<Boolean>.andNotInList(list: Iterable<Triple<T1, T2, T3>>?, expression: Triple<ExpressionWithColumnType<T1>, ExpressionWithColumnType<T2>, ExpressionWithColumnType<T3>>): Op<Boolean> {
    return list?.let { this trueAnd (expression notInList it) } ?: this
}

@JvmName("andNotInListIds")
fun <T : Comparable<T>, ID : EntityID<T>?> Op<Boolean>.andNotInList(list: Iterable<T>?, column: Column<ID>): Op<Boolean> {
    return list?.let { this trueAnd (column notInList it) } ?: this
}


// Extension Operators

fun Op<Boolean>.andDateEq(t: String?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return t?.let { this trueAnd (expression dateEq Date.valueOf(it).toKotlinInstant()) } ?: this
}

fun Op<Boolean>.andDateNeq(t: String?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return t?.let { this trueAnd (expression dateNeq Date.valueOf(it).toKotlinInstant()) } ?: this
}

fun Op<Boolean>.andDateLess(t: String?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return t?.let { this trueAnd (expression less Date.valueOf(it).toKotlinInstant()) } ?: this
}

fun Op<Boolean>.andDateLessEq(t: String?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return t?.let { this trueAnd (expression less (Date.valueOf(it).toKotlinInstant() + 1.days)) } ?: this
}

fun Op<Boolean>.andDateGreater(t: String?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return t?.let { this trueAnd (expression greater Date.valueOf(it).toKotlinInstant()) } ?: this
}

fun Op<Boolean>.andDateGreaterEq(t: String?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return t?.let { this trueAnd (expression greater (Date.valueOf(it).toKotlinInstant() + 1.days)) } ?: this
}

fun Op<Boolean>.andDateInList(list: Iterable<String>?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return list
        ?.map { Date.valueOf(it).toKotlinInstant() }
        ?.let { this trueAnd SingleValueInListOp(expression, it, isInList = true) } ?: this
}

fun Op<Boolean>.andDateNotInList(list: Iterable<String>?, expression: ExpressionWithColumnType<out Instant?>): Op<Boolean> {
    return list
        ?.map { Date.valueOf(it).toKotlinInstant() }
        ?.let { this trueAnd SingleValueInListOp(expression, it, isInList = false) } ?: this
}

private fun String.wrapLikePattern(): String {
    return "%${replace("%", "\\\\%").replace("_", "\\\\_")}%"
}