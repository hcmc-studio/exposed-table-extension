package studio.hcmc.kotlin.protocol.io

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Table
import studio.hcmc.kotlin.protocol.SortOrder
import studio.hcmc.kotlin.protocol.toExposedSortOrder
import kotlin.reflect.full.memberProperties
import org.jetbrains.exposed.sql.SortOrder as ExposedSortOrder

inline fun <reified Order : ListOptionOrder> Order.buildOrder(table: Table): Array<Pair<Expression<*>, ExposedSortOrder>> {
    val list = ArrayList<Pair<Expression<*>, ExposedSortOrder>>()
    for (property in Order::class.memberProperties) {
        val value = property.get(this)
        if (value is SortOrder) {
            val column = table.columns.find { it.name == property.name } ?: continue
            list.add(column to value.toExposedSortOrder())
        }
    }

    return list.toTypedArray()
}

