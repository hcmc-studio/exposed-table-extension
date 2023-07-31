package studio.hcmc.exposed.table;

import org.jetbrains.exposed.sql.Column;
import org.jetbrains.exposed.sql.Table;

class TableUtils {
    /**
     * Call {@link Table#nullable}. Must be cast to <code>Column&lt;T?&gt;</code> when called from Kotlin.
     */
    static <T> Column<T> nullable(Table table, Column<T> column) {
        return table.nullable(column);
    }
}
