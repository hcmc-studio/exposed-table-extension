package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.exposedLogger
import studio.hcmc.exposed.transaction.blockingTransaction
import studio.hcmc.exposed.transaction.suspendedTransaction
import kotlin.reflect.full.memberProperties

fun <T : Table> T.initBlocking(transaction: Transaction? = null): T {
    val table = this
    blockingTransaction(transaction) {
        for (memberProperty in table::class.memberProperties) {
            memberProperty.call(table)
        }
    }

    return table
}

suspend fun <T : Table> T.init(transaction: Transaction? = null): T {
    val table = this
    suspendedTransaction(transaction) {
        for (memberProperty in table::class.memberProperties) {
            memberProperty.call(table)
        }
    }

    return table
}

fun Table.createBlocking(transaction: Transaction? = null): List<String> {
    val table = this
    return blockingTransaction(transaction) {
        table.initBlocking(this)
        SchemaUtils.createMissingTablesAndColumns(table, withLogs = true)
        val missingColumns = SchemaUtils.addMissingColumnsStatements(table, withLogs = true)
        if (missingColumns.isNotEmpty()) {
            exposedLogger.warn("Missing column(s) for table `${tableName}`: $missingColumns")
            execInBatch(missingColumns)
        }

        missingColumns
    }
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