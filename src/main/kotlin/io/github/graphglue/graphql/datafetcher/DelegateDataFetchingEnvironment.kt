package io.github.graphglue.graphql.datafetcher

import graphql.schema.DataFetchingEnvironment
import io.github.graphglue.model.BaseProperty

/**
 * [DataFetchingEnvironment] which delegates all functionality to [parent], except for [getSource], which returns
 * [source] instead
 *
 * @param parent the delegate which handles most [DataFetchingEnvironment] functionality
 * @param source the override for [getSource]
 */
class DelegateDataFetchingEnvironment(
    private val parent: DataFetchingEnvironment, private val source: BaseProperty<*>
) : DataFetchingEnvironment by parent {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> getSource() = source as T
}