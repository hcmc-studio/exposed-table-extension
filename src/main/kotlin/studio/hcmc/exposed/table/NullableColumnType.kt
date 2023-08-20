package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.Column

fun <T : Any> ColumnDelegate<T>.nullable(configure: Column<T?>.() -> Unit = {}) = object : NullableColumnDelegate<T>() {
    override val configure = configure
    override val columnType = this@nullable.columnType
}