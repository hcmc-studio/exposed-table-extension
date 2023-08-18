package studio.hcmc.exposed.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.IColumnType
import org.jetbrains.exposed.sql.Table
import studio.hcmc.kotlin.format.NamingStrategy
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class ColumnDelegate<T> : ReadOnlyProperty<Table, Column<T>> {
    companion object {
        var namingStrategy = NamingStrategy.CamelCase
    }

    abstract val configure: Column<T>.() -> Unit
    abstract val columnType: IColumnType

    private lateinit var column: Column<T>

    override fun getValue(thisRef: Table, property: KProperty<*>): Column<T> {
        if (this::column.isInitialized) {
            return column
        }

        val name = namingStrategy.convert(property.name)
        column = thisRef.registerColumn(name, columnType)
        column.configure()

        return column
    }
}

fun <T : Comparable<T>> ColumnDelegate<T>.entityId(configure: Column<EntityID<T>>.() -> Unit = {}): ColumnDelegate<EntityID<T>> {
    return object : ColumnDelegate<EntityID<T>>() {
        override val configure get() = throw UnsupportedOperationException()
        override val columnType get() = throw UnsupportedOperationException()

        private lateinit var column: Column<EntityID<T>>

        override fun getValue(thisRef: Table, property: KProperty<*>): Column<EntityID<T>> {
            if (this::column.isInitialized) {
                return column
            }

            column = this@entityId.getValue(thisRef, property).let { TableUtils.entityId(it) }
            column.configure()

            return column
        }
    }
}