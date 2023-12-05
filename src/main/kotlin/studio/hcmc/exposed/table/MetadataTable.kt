package studio.hcmc.exposed.table

import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.Column
import java.util.UUID

abstract class MetadataTable<T : Comparable<T>>(original: IdTable<T>, name: String = "") : IdTable<T>(name) {
    final override val id: Column<EntityID<T>> by referenceByIdColumn(original.id)
    override val primaryKey = PrimaryKey(id)
}

abstract class IntMetadataTable(original: IntIdTable, name: String = "") : MetadataTable<Int>(original, name)

abstract class LongMetadataTable(original: LongIdTable, name: String = "") : MetadataTable<Long>(original, name)

abstract class UUIDMetadataTable(original: UUIDTable, name: String = "") : MetadataTable<UUID>(original, name)
