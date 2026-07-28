package com.marchenaya.runique.navigation.dynamicfeature

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

abstract class DynamicModule(
    private val entryBuilderClassName: String,
    val moduleName: String
) {
    private var cachedEntryBuilder: DynamicModuleEntryBuilder? = null

    internal fun getEntryBuilder(): DynamicModuleEntryBuilder {
        return cachedEntryBuilder ?: (Class.forName(entryBuilderClassName)
            .getConstructor()
            .newInstance() as DynamicModuleEntryBuilder)
            .also { cachedEntryBuilder = it }
    }
}

fun interface DynamicModuleEntryBuilder {
    fun EntryProviderScope<NavKey>.build(onBack: () -> Unit)
}

fun EntryProviderScope<NavKey>.buildDynamicEntries(
    module: DynamicModule,
    onBack: () -> Unit
) {
    with(module.getEntryBuilder()) {
        build(onBack)
    }
}
