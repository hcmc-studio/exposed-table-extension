package studio.hcmc.kotlin.protocol

import org.jetbrains.exposed.sql.SortOrder as ExposedSortOrder
import studio.hcmc.kotlin.protocol.SortOrder as HcmcSortOrder

fun HcmcSortOrder.toExposedSortOrder(): ExposedSortOrder {
    return ExposedSortOrder.entries[ordinal]
}