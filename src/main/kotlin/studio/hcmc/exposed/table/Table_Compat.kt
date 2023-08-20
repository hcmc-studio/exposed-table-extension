package studio.hcmc.exposed.table

import org.jetbrains.exposed.sql.AutoIncColumnType
import org.jetbrains.exposed.sql.IColumnType
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

internal fun IColumnType.cloneAsBaseType(): IColumnType {
    return ((this as? AutoIncColumnType)?.delegate ?: this).clone()
}

internal fun <T : Any> T.clone(replaceArgs: Map<KProperty1<T, *>, Any> = emptyMap()): T = javaClass.kotlin.run {
    val consParams = primaryConstructor!!.parameters
    val mutableProperties = memberProperties.filterIsInstance<KMutableProperty1<T, Any?>>()
    val allValues = memberProperties
        .filter { it in mutableProperties || it.name in consParams.map(KParameter::name) }
        .associate { it.name to (replaceArgs[it] ?: it.get(this@clone)) }
    primaryConstructor!!.callBy(consParams.associateWith { allValues[it.name] }).also { newInstance ->
        for (prop in mutableProperties) {
            prop.set(newInstance, allValues[prop.name])
        }
    }
}