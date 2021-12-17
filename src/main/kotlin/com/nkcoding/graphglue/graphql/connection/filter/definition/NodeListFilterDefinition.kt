package com.nkcoding.graphglue.graphql.connection.filter.definition

import com.nkcoding.graphglue.graphql.connection.filter.model.*
import com.nkcoding.graphglue.graphql.extensions.getSimpleName
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

class NodeListFilterDefinition(name: String, nodeType: KType, subFilterGenerator: SubFilterGenerator) :
    SimpleObjectFilterDefinitionEntry<NodeSubFilterDefinition>(
        name, "", "${nodeType.jvmErasure.getSimpleName()}ListFilterInput",
        listOf(
            NodeSubFilterDefinition(
                "all",
                "Filters for nodes where all of the related nodes match this filter",
                nodeType,
                subFilterGenerator
            ),
            NodeSubFilterDefinition(
                "some",
                "Filters for nodes where some of the related nodes match this filter",
                nodeType,
                subFilterGenerator
            ),
            NodeSubFilterDefinition(
                "none",
                "Filters for nodes where none of the related nodes match this filter",
                nodeType,
                subFilterGenerator
            )
        )
    ) {

    override fun parseEntry(value: Any?): FilterEntry {
        value as Map<*, *>
        val entries = value.map {
            val (name, entry) = it
            val definition = fields[name] ?: throw IllegalStateException("Unknown input")
            val filter = definition.parseEntry(entry)
            when(name) {
                "all" -> AllNodeListFilterEntry(definition, filter)
                "some" -> SomeNodeListFilterEntry(definition, filter)
                "none" -> NoneNodeListFilterEntry(definition, filter)
                else -> throw IllegalStateException("Unknown NodeListFilter entry")
            }
        }
        return NodeListFilter(this, entries)
    }
}

