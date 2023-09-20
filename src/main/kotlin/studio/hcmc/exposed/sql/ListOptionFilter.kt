package studio.hcmc.exposed.sql

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.bitwiseAnd
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import studio.hcmc.kotlin.protocol.BitMask
import studio.hcmc.kotlin.protocol.io.ListOptionFilter
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun <T> ListOptionFilter.NumericElement<T>.buildOp(column: Column<T>): Op<Boolean> where T : Number, T : Comparable<T> {
    return Op.TRUE
        .andEq(eq, column)
        .andNeq(neq, column)
        .andLess(less, column)
        .andLessEq(lessEq, column)
        .andGreater(greater, column)
        .andGreaterEq(greaterEq, column)
        .andInList(inList, column)
        .andNotInList(notInList, column)
}

@JvmName("buildOpEntityID")
fun <T> ListOptionFilter.NumericElement<T>.buildOp(column: Column<EntityID<T>>): Op<Boolean> where T : Number, T : Comparable<T> {
    return Op.TRUE
        .andEq(eq, column)
        .andNeq(neq, column)
        .andLess(less, column)
        .andLessEq(lessEq, column)
        .andGreater(greater, column)
        .andGreaterEq(greaterEq, column)
        .andInList(inList, column)
        .andNotInList(notInList, column)
}

fun ListOptionFilter.StringElement.buildOp(column: Column<String>): Op<Boolean> {
    return Op.TRUE
        .andEq(eq, column)
        .andNeq(neq, column)
        .andLess(less, column)
        .andLessEq(lessEq, column)
        .andGreater(greater, column)
        .andGreaterEq(greaterEq, column)
        .andLike(like, column)
        .andNotLike(notLike, column)
        .andInList(inList, column)
        .andNotInList(notInList, column)
}

@JvmName("buildOpEntityID")
fun ListOptionFilter.StringElement.buildOp(column: Column<EntityID<String>>): Op<Boolean> {
    return Op.TRUE
        .andEq(eq, column)
        .andNeq(neq, column)
        .andLess(less, column)
        .andLessEq(lessEq, column)
        .andGreater(greater, column)
        .andGreaterEq(greaterEq, column)
        .andLike(like, column)
        .andNotLike(notLike, column)
        .andInList(inList, column)
        .andNotInList(notInList, column)
}

fun ListOptionFilter.DateElement.buildOp(column: Column<Instant>): Op<Boolean> {
    return Op.TRUE
        .andDateEq(eq, column)
        .andDateNeq(neq, column)
        .andDateLess(less, column)
        .andDateLessEq(lessEq, column)
        .andDateGreater(greater, column)
        .andDateGreaterEq(greaterEq, column)
        .andDateInList(inList, column)
        .andDateNotInList(notInList, column)
}

fun ListOptionFilter.BitMaskElement<*>.buildOp(column: Column<BitMask<*>>): Op<Boolean> {
    var op = Op.TRUE
        .andEq(eq, column)
        .andNeq(neq, column)
    op = includeAll?.let { op trueAnd ((column bitwiseAnd it) eq it) } ?: op
    op = includeAny?.let { op trueAnd ((column bitwiseAnd it) neq 0) } ?: op
    op = excludeAll?.let { op trueAnd ((column bitwiseAnd it) eq 0) } ?: op

    return op
}

@Suppress("UNCHECKED_CAST")
fun ListOptionFilter.buildOp(table: Table): Op<Boolean> {
    val columns = table.columns.associateBy { it.name }
    val clazz = this::class as KClass<ListOptionFilter>
    var op: Op<Boolean> = Op.TRUE
    for (property in clazz.memberProperties) {
        val element = property.get(this) as? ListOptionFilter.Element ?: continue
        val column = columns[property.name] ?: continue
        when (element) {
            is ListOptionFilter.NumericElement<*> -> {
                val elementClazz = element::class as KClass<ListOptionFilter.NumericElement<*>>
                for (elementProperty in elementClazz.memberProperties) {
                    val elementValue = elementProperty.get(element) ?: continue
                    when (elementValue) {
                        is Byte -> {
                            op = op and if (property.name == "id" || property.name.endsWith("Id")) {
                                (element as ListOptionFilter.NumericElement<Byte>).buildOp(column as Column<EntityID<Byte>>)
                            } else {
                                (element as ListOptionFilter.NumericElement<Byte>).buildOp(column as Column<Byte>)
                            }
                            break
                        }
                        is Short -> {
                            op = op and if (property.name == "id" || property.name.endsWith("Id")) {
                                (element as ListOptionFilter.NumericElement<Short>).buildOp(column as Column<EntityID<Short>>)
                            } else {
                                (element as ListOptionFilter.NumericElement<Short>).buildOp(column as Column<Short>)
                            }
                            break
                        }
                        is Int -> {
                            op = op and if (property.name == "id" || property.name.endsWith("Id")) {
                                (element as ListOptionFilter.NumericElement<Int>).buildOp(column as Column<EntityID<Int>>)
                            } else {
                                (element as ListOptionFilter.NumericElement<Int>).buildOp(column as Column<Int>)
                            }
                            break
                        }
                        is Long -> {
                            op = op and if (property.name == "id" || property.name.endsWith("Id")) {
                                (element as ListOptionFilter.NumericElement<Long>).buildOp(column as Column<EntityID<Long>>)
                            } else {
                                (element as ListOptionFilter.NumericElement<Long>).buildOp(column as Column<Long>)
                            }
                            break
                        }
                        is Float -> {
                            op = op and if (property.name == "id" || property.name.endsWith("Id")) {
                                (element as ListOptionFilter.NumericElement<Float>).buildOp(column as Column<EntityID<Float>>)
                            } else {
                                (element as ListOptionFilter.NumericElement<Float>).buildOp(column as Column<Float>)
                            }
                            break
                        }
                        is Double -> {
                            op = op and if (property.name == "id" || property.name.endsWith("Id")) {
                                (element as ListOptionFilter.NumericElement<Double>).buildOp(column as Column<EntityID<Double>>)
                            } else {
                                (element as ListOptionFilter.NumericElement<Double>).buildOp(column as Column<Double>)
                            }
                            break
                        }
                    }
                }
            }
            is ListOptionFilter.StringElement -> {
                op = op and if (property.name == "id" || property.name.endsWith("Id")) {
                    element.buildOp(column as Column<EntityID<String>>)
                } else {
                    element.buildOp(column as Column<String>)
                }
            }
            is ListOptionFilter.DateElement -> {
                op = op and element.buildOp(column as Column<Instant>)
            }
            is ListOptionFilter.BitMaskElement<*> -> {
                op = op and element.buildOp(column as Column<BitMask<*>>)
            }
        }
    }

    return op
}