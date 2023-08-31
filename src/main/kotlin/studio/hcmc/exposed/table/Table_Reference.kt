package studio.hcmc.exposed.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import kotlin.reflect.KProperty

@Deprecated(
    message = "Use referenceByIdColumn instead.",
    replaceWith = ReplaceWith("this.referenceByIdColumn(refColumn, onDelete, onUpdate, fkName, configure)")
)
fun <T : Comparable<T>, E : EntityID<T>> Table.reference(
    refColumn: Column<E>,
    onDelete: ReferenceOption? = null,
    onUpdate: ReferenceOption? = null,
    fkName: String? = null,
    configure: Column<T>.() -> Unit = {}
) = object : ColumnDelegate<T>() {
    override val configure = configure
    override val columnType = refColumn.columnType.rawSqlType()

    private lateinit var column: Column<T>

    override fun getValue(thisRef: Table, property: KProperty<*>): Column<T> {
        if (this::column.isInitialized) {
            return column
        }

        val name = namingStrategy.convert(property.name)
        column = thisRef.registerColumn(name, refColumn.columnType.rawSqlType())
        column.foreignKey = ForeignKeyConstraint(refColumn, column, onUpdate, onDelete, fkName)
        column.configure()

        return column
    }
}

@Deprecated(
    message = "Use optReferenceByIdColumn instead.",
    replaceWith = ReplaceWith("this.optReferenceByIdColumn(refColumn, onDelete, onUpdate, fkName, configure)")
)
fun <T : Comparable<T>, E : EntityID<T>> Table.optReference(
    refColumn: Column<E>,
    onDelete: ReferenceOption? = null,
    onUpdate: ReferenceOption? = null,
    fkName: String? = null,
    configure: Column<T?>.() -> Unit = {}
) = object : ColumnDelegate<T?>() {
    override val configure = configure
    override val columnType = refColumn.columnType.rawSqlType()

    private lateinit var column: Column<T?>

    override fun getValue(thisRef: Table, property: KProperty<*>): Column<T?> {
        if (this::column.isInitialized) {
            return column
        }

        val name = namingStrategy.convert(property.name)
        column = thisRef.registerColumn<T>(name, refColumn.columnType.rawSqlType()).nullable()
        column.foreignKey = ForeignKeyConstraint(refColumn, column, onUpdate, onDelete, fkName)
        column.configure()

        return column
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Comparable<T>, E : EntityID<T>> Table.referenceByIdColumn(
    refColumn: Column<E>,
    onDelete: ReferenceOption? = null,
    onUpdate: ReferenceOption? = null,
    fkName: String? = null,
    configure: Column<E>.() -> Unit = {}
) = object : ColumnDelegate<E>() {
    private val idColumn get() = (refColumn.columnType as EntityIDColumnType<T>).idColumn
    override val configure = configure
    override val columnType = EntityIDColumnType(idColumn)

    private lateinit var column: Column<E>

    override fun getValue(thisRef: Table, property: KProperty<*>): Column<E> {
        if (this::column.isInitialized) {
            return column
        }

        val name = namingStrategy.convert(property.name)
        column = entityId(name, idColumn) as Column<E>
        column = column.references(refColumn, onDelete, onUpdate, fkName)
        column.configure()

        return column
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Comparable<T>, E : EntityID<T>> Table.optReferenceByIdColumn(
    refColumn: Column<E>,
    onDelete: ReferenceOption? = null,
    onUpdate: ReferenceOption? = null,
    fkName: String? = null,
    configure: Column<E?>.() -> Unit = {}
) = object : ColumnDelegate<E?>() {
    private val idColumn get() = (refColumn.columnType as EntityIDColumnType<T>).idColumn
    override val configure = configure
    override val columnType = EntityIDColumnType(idColumn)

    private lateinit var original: Column<E>
    private lateinit var column: Column<E?>

    override fun getValue(thisRef: Table, property: KProperty<*>): Column<E?> {
        if (this::column.isInitialized) {
            return column
        }

        val name = namingStrategy.convert(property.name)
        original = entityId(name, idColumn) as Column<E>
        column = original.references(refColumn, onDelete, onUpdate, fkName).nullable()
        column.configure()

        return column
    }
}
