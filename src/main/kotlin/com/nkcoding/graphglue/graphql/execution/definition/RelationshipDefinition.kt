package com.nkcoding.graphglue.graphql.execution.definition

import com.nkcoding.graphglue.graphql.extensions.getDelegateAccessible
import com.nkcoding.graphglue.graphql.extensions.getPropertyName
import com.nkcoding.graphglue.model.Node
import com.nkcoding.graphglue.model.NodeProperty
import com.nkcoding.graphglue.model.NodeRelationship
import com.nkcoding.graphglue.neo4j.execution.NodeQueryResult
import org.springframework.data.neo4j.core.schema.Relationship
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

abstract class RelationshipDefinition(
    val property: KProperty1<*, *>,
    val nodeKClass: KClass<out Node>,
    val type: String,
    val direction: Relationship.Direction,
    private val parentKClass: KClass<*>
) {
    val graphQLName get() = property.getPropertyName(parentKClass)
    private val remotePropertySetter: RemotePropertySetter? = generateRemotePropertySetter()

    private fun generateRemotePropertySetter(): RemotePropertySetter? {
        for (remoteProperty in nodeKClass.memberProperties) {
            val annotation = remoteProperty.findAnnotation<NodeRelationship>()
            if (annotation?.type == type && annotation.direction != direction) {
                return { remoteNode, value ->
                    val nodeProperty = remoteProperty.getDelegateAccessible<NodeProperty<Node>>(remoteNode)
                    nodeProperty.setFromRemote(value)
                }
            }
        }
        return null
    }

    fun generateRelationship(
        rootNode: org.neo4j.cypherdsl.core.Node,
        propertyNode: org.neo4j.cypherdsl.core.Node
    ): org.neo4j.cypherdsl.core.Relationship {
        return when (direction) {
            Relationship.Direction.OUTGOING -> rootNode.relationshipTo(propertyNode, type)
            Relationship.Direction.INCOMING -> rootNode.relationshipFrom(propertyNode, type)
        }
    }

    internal fun <T : Node> registerQueryResult(node: Node, nodeQueryResult: NodeQueryResult<T>) {
        registerLocalQueryResult(node, nodeQueryResult)
        val setter = remotePropertySetter
        if (setter != null) {
            for (remoteNode in nodeQueryResult.nodes) {
                setter(remoteNode, node)
            }
        }
    }

    internal abstract fun <T : Node> registerLocalQueryResult(node: Node, nodeQueryResult: NodeQueryResult<T>)
}

private typealias RemotePropertySetter = (remoteNode: Node, value: Node) -> Unit