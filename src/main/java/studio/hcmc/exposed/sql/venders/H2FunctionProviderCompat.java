package studio.hcmc.exposed.sql.venders;

import org.jetbrains.exposed.sql.Expression;
import org.jetbrains.exposed.sql.IColumnType;
import org.jetbrains.exposed.sql.QueryBuilder;

@SuppressWarnings("ALL")
public class H2FunctionProviderCompat {
    public static void cast(Expression<?> expr, IColumnType type, QueryBuilder builder) {
        org.jetbrains.exposed.sql.vendors.H2FunctionProvider.INSTANCE.cast(expr, type, builder);
    }
}