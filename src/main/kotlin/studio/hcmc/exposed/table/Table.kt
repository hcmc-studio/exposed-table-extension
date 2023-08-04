package studio.hcmc.exposed.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import studio.hcmc.exposed.transaction.blockingTransaction
import studio.hcmc.exposed.transaction.suspendedTransaction
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.full.memberProperties

fun <T : Comparable<T>, E : EntityID<T>> Table.reference(
    refColumn: Column<E>,
    onDelete: ReferenceOption? = null,
    onUpdate: ReferenceOption? = null,
    fkName: String? = null,
    configure: Column<T>.() -> Unit = {}
): ReadOnlyProperty<Table, Column<T>> = ReadOnlyProperty { table, property ->
    val name = property.name
    ColumnDelegate.findPresent(table, name) ?: table
        .registerColumn<T>(name, refColumn.columnType.rawSqlType())
        .apply { foreignKey = ForeignKeyConstraint(refColumn, this, onUpdate, onDelete, fkName) }
        .apply(configure)
}

fun <T : Comparable<T>, E : EntityID<T>> Table.optReference(
    refColumn: Column<E>,
    onDelete: ReferenceOption? = null,
    onUpdate: ReferenceOption? = null,
    fkName: String? = null,
    configure: Column<T?>.() -> Unit = {}
): ReadOnlyProperty<Table, Column<T?>> = ReadOnlyProperty { table, property ->
    val name = property.name
    NullableColumnDelegate.findPresent<T>(table, name) ?: table
        .registerColumn<T>(name, refColumn.columnType.rawSqlType())
        .nullable()
        .apply { foreignKey = ForeignKeyConstraint(refColumn, this, onUpdate, onDelete, fkName) }
        .apply(configure)
}

suspend fun Table.create(transaction: Transaction? = null): List<String> {
    val table = this
    return suspendedTransaction(transaction) {
        for (memberProperty in table::class.memberProperties) {
            memberProperty.call(table)
        }

        SchemaUtils.create(table)
        val missingColumns = SchemaUtils.addMissingColumnsStatements(table)
        if (missingColumns.isNotEmpty()) {
            exposedLogger.warn("Missing column(s) for table `${tableName}`: $missingColumns")
            execInBatch(missingColumns)
        }

        missingColumns
    }
}