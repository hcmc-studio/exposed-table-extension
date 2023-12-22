package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import studio.hcmc.kotlin.protocol.BitMask
import studio.hcmc.kotlin.protocol.BitMaskFlag

fun <E> Table.bitMask(name: String): Column<BitMask<E>> where E : Enum<E>, E : BitMaskFlag {
    return registerColumn(name, BitMaskColumnType<E>())
}