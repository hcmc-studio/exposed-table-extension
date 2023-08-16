package studio.hcmc.exposed.table

import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Column

interface DeletableTable {
    val deletedAt: Column<Instant?>
}