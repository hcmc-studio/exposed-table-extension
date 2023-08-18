package studio.hcmc.exposed.table

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.*
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.vendors.currentDialect
import studio.hcmc.kotlin.protocol.BitMask
import studio.hcmc.kotlin.protocol.BitMaskFlag
import java.math.BigDecimal
import java.sql.ResultSet
import java.util.*
import kotlin.time.Duration

fun Table.byte(configure: Column<Byte>.() -> Unit = {}) = object : ColumnDelegate<Byte>() {
    override val configure = configure
    override val columnType = ByteColumnType()
}

fun Table.ubyte(configure: Column<UByte>.() -> Unit = {}) = object : ColumnDelegate<UByte>() {
    override val configure = configure
    override val columnType = UByteColumnType()
}

fun Table.short(configure: Column<Short>.() -> Unit = {}) = object : ColumnDelegate<Short>() {
    override val configure = configure
    override val columnType = ShortColumnType()
}

fun Table.ushort(configure: Column<UShort>.() -> Unit = {}) = object : ColumnDelegate<UShort>() {
    override val configure = configure
    override val columnType = UShortColumnType()
}

fun Table.integer(configure: Column<Int>.() -> Unit = {}) = object : ColumnDelegate<Int>() {
    override val configure = configure
    override val columnType = IntegerColumnType()
}

fun Table.uinteger(configure: Column<UInt>.() -> Unit = {}) = object : ColumnDelegate<UInt>() {
    override val configure = configure
    override val columnType = UIntegerColumnType()
}

fun Table.long(configure: Column<Long>.() -> Unit = {}) = object : ColumnDelegate<Long>() {
    override val configure = configure
    override val columnType = LongColumnType()
}

fun Table.ulong(configure: Column<ULong>.() -> Unit = {}) = object : ColumnDelegate<ULong>() {
    override val configure = configure
    override val columnType = ULongColumnType()
}

fun Table.float(configure: Column<Float>.() -> Unit = {}) = object : ColumnDelegate<Float>() {
    override val configure = configure
    override val columnType = FloatColumnType()
}

fun Table.double(configure: Column<Double>.() -> Unit = {}) = object : ColumnDelegate<Double>() {
    override val configure = configure
    override val columnType = DoubleColumnType()
}

fun Table.decimal(precision: Int, scale: Int, configure: Column<BigDecimal>.() -> Unit = {}) = object : ColumnDelegate<BigDecimal>() {
    override val configure = configure
    override val columnType = DecimalColumnType(precision, scale)
}

fun Table.char(configure: Column<Char>.() -> Unit = {}) = object : ColumnDelegate<Char>() {
    override val configure = configure
    override val columnType = CharacterColumnType()
}

fun Table.char(length: Int, collate: String? = null, configure: Column<String>.() -> Unit = {}) = object : ColumnDelegate<String>() {
    override val configure = configure
    override val columnType = CharColumnType(length, collate)
}

fun Table.varchar(length: Int, collate: String? = null, configure: Column<String>.() -> Unit = {}) = object : ColumnDelegate<String>() {
    override val configure = configure
    override val columnType = VarCharColumnType(length, collate)
}

fun Table.text(collate: String? = null, eagerLoading: Boolean = false, configure: Column<String>.() -> Unit = {}) = object : ColumnDelegate<String>() {
    override val configure = configure
    override val columnType = TextColumnType(collate, eagerLoading)
}

fun Table.mediumText(collate: String? = null, eagerLoading: Boolean = false, configure: Column<String>.() -> Unit = {}) = object : ColumnDelegate<String>() {
    override val configure = configure
    override val columnType = MediumTextColumnType(collate, eagerLoading)
}

fun Table.largeText(collate: String? = null, eagerLoading: Boolean = false, configure: Column<String>.() -> Unit = {}) = object : ColumnDelegate<String>() {
    override val configure = configure
    override val columnType = LargeTextColumnType(collate, eagerLoading)
}

fun Table.binary(configure: Column<ByteArray>.() -> Unit = {}) = object : ColumnDelegate<ByteArray>() {
    override val configure = configure
    override val columnType = BasicBinaryColumnType()
}

fun Table.binary(length: Int, configure: Column<ByteArray>.() -> Unit = {}) = object : ColumnDelegate<ByteArray>() {
    override val configure = configure
    override val columnType = BinaryColumnType(length)
}

fun Table.blob(configure: Column<ExposedBlob>.() -> Unit = {}) = object : ColumnDelegate<ExposedBlob>() {
    override val configure = configure
    override val columnType = BlobColumnType()
}

fun Table.uuid(configure: Column<UUID>.() -> Unit = {}) = object : ColumnDelegate<UUID>() {
    override val configure = configure
    override val columnType = UUIDColumnType()
}

fun Table.bool(configure: Column<Boolean>.() -> Unit = {}) = object : ColumnDelegate<Boolean>() {
    override val configure = configure
    override val columnType = BooleanColumnType()
}

inline fun <reified T : Enum<T>> Table.enumeration(noinline configure: Column<T>.() -> Unit = {}) = object : ColumnDelegate<T>() {
    override val configure = configure
    override val columnType = EnumerationColumnType(T::class)
}

inline fun <reified T : Enum<T>> Table.enumerationByName(length: Int, noinline configure: Column<T>.() -> Unit = {}) = object : ColumnDelegate<T>() {
    override val configure = configure
    override val columnType = EnumerationNameColumnType(T::class, length)
}

fun Table.date(configure: Column<LocalDate>.() -> Unit = {}) = object : ColumnDelegate<LocalDate>() {
    override val configure = configure
    override val columnType = KotlinLocalDateColumnType()
}

fun Table.datetime(configure: Column<LocalDateTime>.() -> Unit = {}) = object : ColumnDelegate<LocalDateTime>() {
    override val configure = configure
    override val columnType = KotlinLocalDateTimeColumnType()
}

fun Table.time(configure: Column<LocalTime>.() -> Unit = {}) = object : ColumnDelegate<LocalTime>() {
    override val configure = configure
    override val columnType = KotlinLocalTimeColumnType()
}

fun Table.timestamp(configure: Column<Instant>.() -> Unit = {}) = object : ColumnDelegate<Instant>() {
    override val configure = configure
    override val columnType = KotlinInstantColumnType()
}

fun Table.duration(configure: Column<Duration>.() -> Unit = {}) = object : ColumnDelegate<Duration>() {
    override val configure = configure
    override val columnType = KotlinDurationColumnType()
}

fun <E> Table.bitMask(configure: Column<BitMask<E>>.() -> Unit = {}) where E : Enum<E>, E : BitMaskFlag = object : ColumnDelegate<BitMask<E>>() {
    override val configure = configure
    override val columnType = BitMaskColumnType<E>()

}

class BitMaskColumnType<E> : ColumnType() where E : Enum<E>, E : BitMaskFlag {
    override fun sqlType(): String {
        return currentDialect.dataTypeProvider.integerType()
    }

    override fun nonNullValueToString(value: Any): String {
        return when (value) {
            is String -> value
            is BitMask<*> -> value.value.toString()
            else -> error("Unexpected value: $value of ${value::class.qualifiedName}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun valueFromDB(value: Any): BitMask<E> {
        return when (value) {
            is BitMask<*> -> value as BitMask<E>
            is Int -> BitMask(value)
            is String -> BitMask(value.toInt())
            else -> valueFromDB(value.toString())
        }
    }

    override fun readObject(rs: ResultSet, index: Int): Any? {
        return rs.getInt(index)
    }

    override fun notNullValueToDB(value: Any): Any {
        return when {
            value is BitMask<*> -> value.value
            else -> value
        }
    }
}