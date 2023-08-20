package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.Table
import studio.hcmc.kotlin.format.NamingStrategy
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class NullableColumnDelegate<T : Any> : ReadOnlyProperty<Table, Column<T?>> {
    companion object {
        var namingStrategy = NamingStrategy.CamelCase
    }

    abstract val configure: Column<T?>.() -> Unit
    abstract val columnType: IColumnType

    private lateinit var column: Column<T?>

    override fun getValue(thisRef: Table, property: KProperty<*>): Column<T?> {
        if (this::column.isInitialized) {
            return column
        }

        val name = namingStrategy.convert(property.name)
        column = thisRef.registerColumn(name, columnType)
        column = TableUtils.nullable(thisRef, column)
        column.configure()

        return column
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> NullableColumnDelegate.Companion.findPresent(table: Table, name: String): Column<T?>? {
    return table.columns
        .find { it.name == name }
        ?.let { it as Column<T?> }
}