package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.Table
import studio.hcmc.kotlin.format.NamingStrategy
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface ColumnDelegate<T> : ReadOnlyProperty<Table, Column<T>> {
    companion object {
        var namingStrategy = NamingStrategy.CamelCase
    }

    val configure: Column<T>.() -> Unit
    val columnType: IColumnType

    override fun getValue(thisRef: Table, property: KProperty<*>): Column<T> {
        val name = namingStrategy.convert(property.name)
        findPresent<T>(thisRef, name)?.let { return it }

        return thisRef.registerColumn<T>(name, columnType).apply(configure)
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T> ColumnDelegate.Companion.findPresent(table: Table, name: String): Column<T>? {
    return table.columns
        .find { it.name == name }
        ?.let { it as Column<T> }
}