package io.github.graphglue

import com.expediagroup.graphql.server.spring.GraphQLAutoConfiguration
import io.github.graphglue.authorization.GraphglueAuthorizationConfiguration
import io.github.graphglue.connection.GraphglueConnectionConfiguration
import io.github.graphglue.data.GraphglueDataConfiguration
import io.github.graphglue.graphql.GraphglueGraphQLConfiguration
import io.github.graphglue.model.GraphglueModelConfiguration
import io.github.graphglue.model.NODE_ID_GENERATOR_BEAN
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.neo4j.Neo4jAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.neo4j.core.support.UUIDStringGenerator

/**
 * Manages Spring boot autoconfiguration for all Graphglue related nodes
 * Imports the GraphglueGraphQLConfiguration and GraphglueNeo4jConfiguration
 */
@Configuration
@Import(
    GraphglueModelConfiguration::class,
    GraphglueGraphQLConfiguration::class,
    GraphglueDataConfiguration::class,
    GraphglueAuthorizationConfiguration::class,
    GraphglueConnectionConfiguration::class
)
@AutoConfigureBefore(
    value = [GraphQLAutoConfiguration::class]
)
@AutoConfigureAfter(
    value = [Neo4jAutoConfiguration::class, Neo4jReactiveDataAutoConfiguration::class]
)
class GraphglueAutoConfiguration