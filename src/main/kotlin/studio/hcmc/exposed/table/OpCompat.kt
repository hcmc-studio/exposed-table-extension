package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.vendors.H2Dialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import org.jetbrains.exposed.sql.vendors.h2Mode
import studio.hcmc.exposed.sql.venders.H2FunctionProviderCompat

fun <T> ExpressionWithColumnType<T>.castToExpressionTypeForH2BitWiseIps(e: Expression<out T>, queryBuilder: QueryBuilder) {
    when {
        currentDialect.h2Mode == H2Dialect.H2CompatibilityMode.Oracle -> H2FunctionProviderCompat.cast(e, ByteColumnType(), queryBuilder)
        e is Column<*> || e is LiteralOp<*> -> queryBuilder.append(e)
        else -> currentDialect.functionProvider.cast(e, columnType, queryBuilder)
    }
}