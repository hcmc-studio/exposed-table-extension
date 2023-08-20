package studio.hcmc.exposed.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import studio.hcmc.exposed.transaction.blockingTransaction
import studio.hcmc.exposed.transaction.suspendedTransaction
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.full.memberProperties

suspend fun <T : Table> T.init(transaction: Transaction? = null): T {
    val table = this
    suspendedTransaction(transaction) {
        for (memberProperty in table::class.memberProperties) {
            memberProperty.call(table)
        }
    }

    return table
}

suspend fun Table.create(transaction: Transaction? = null): List<String> {
    val table = this
    return suspendedTransaction(transaction) {
        table.init(this)
        SchemaUtils.create(table)
        val missingColumns = SchemaUtils.addMissingColumnsStatements(table)
        if (missingColumns.isNotEmpty()) {
            exposedLogger.warn("Missing column(s) for table `${tableName}`: $missingColumns")
            execInBatch(missingColumns)
        }

        missingColumns
    }
}

suspend fun Table.drop(transaction: Transaction? = null) {
    val table = this
    suspendedTransaction(transaction) {
        table.init(this)
        SchemaUtils.drop(table)
    }
}