package studio.hcmc.exposed.table;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.exposed.dao.id.EntityID;
import org.jetbrains.exposed.sql.Column;
import org.jetbrains.exposed.sql.Table;

class TableUtils {
    /**
     * Call {@link Table#nullable}. Must be cast to <code>Column&lt;T?&gt;</code> when called from Kotlin.
     */
    static <T> Column<T> nullable(Table table, Column<T> column) {
        return table.nullable(column);
    }

    static <T extends Comparable<? super T>> Column<EntityID<T>> entityId(Column<T> column) {
        return column.getTable().entityId(column);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    static <T> Column<T> findPresent(Table table, String name) {
        final var columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            final var column = columns.get(i);
            final var columnName = column.getName();
            if (columnName.equals(name)) {
                return (Column<T>) column;
            }
        }

        return null;
    }
}
