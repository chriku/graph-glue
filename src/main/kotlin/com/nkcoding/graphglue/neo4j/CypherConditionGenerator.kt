package com.nkcoding.graphglue.neo4j

import org.neo4j.cypherdsl.core.Condition
import org.neo4j.cypherdsl.core.Node

interface CypherConditionGenerator {
    fun generateCondition(node: Node): Condition
}