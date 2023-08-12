package studio.hcmc.kotlin.protocol

import studio.hcmc.exposed.table.ExposedSortOrder

fun SortOrder.toExposedSortOrder(): ExposedSortOrder {
    return ExposedSortOrder.entries[ordinal]
}