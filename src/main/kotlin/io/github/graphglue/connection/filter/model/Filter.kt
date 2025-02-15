package io.github.graphglue.connection.filter.model

import io.github.graphglue.data.execution.CypherConditionGenerator
import org.neo4j.cypherdsl.core.Node

/**
 * Parsed version of an overall filter
 *
 * @param metaFilter the top level parsed filter
 */
data class Filter(val metaFilter: MetaFilter) : CypherConditionGenerator {
    override fun generateCondition(node: Node) = metaFilter.generateCondition(node)
}