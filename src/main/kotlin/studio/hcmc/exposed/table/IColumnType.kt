package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.AutoIncColumnType
import org.jetbrains.exposed.sql.EntityIDColumnType
import org.jetbrains.exposed.sql.IColumnType

internal fun IColumnType.rawSqlType() = when (this) {
    is AutoIncColumnType -> delegate
    is EntityIDColumnType<*> -> (idColumn.columnType as? AutoIncColumnType)?.delegate ?: this
    else -> this
}