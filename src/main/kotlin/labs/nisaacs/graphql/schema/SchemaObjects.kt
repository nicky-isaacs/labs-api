package labs.nisaacs.graphql.schema

import graphql.schema.DataFetchingEnvironment
import graphql.schema.DelegatingDataFetchingEnvironment
import graphql.schema.idl.TypeRuntimeWiring
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.function.UnaryOperator

/**
 * A marker trait signifying that a model represent a schema object
 */
sealed interface SchemaObject

data class User(
    val id: Long,
    val email: String
) : SchemaObject

data class Book(val id: Long, val title: String) : SchemaObject

fun <T : SchemaObject> joiner(): Joiner<T> {
    return object : Joiner<T> {}
}

/**
 * Allows extending
 */
interface Joiner<T : SchemaObject> : UnaryOperator<TypeRuntimeWiring.Builder> {

    fun <V : SchemaObject> join(
        name: String,
        f: suspend (T, DataFetchingEnvironment) -> V
    ): Joiner<T> {
        return object : Joiner<T> {
            override fun apply(t: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder {
                return this@Joiner.apply(t).dataFetcher(name) {
                    GlobalScope.future {
                        f(it.getSource(), disallowGetSource(it))
                    }
                }

            }
        }
    }

    fun <V : SchemaObject> joinList(
        name: String,
        f: suspend (T, DataFetchingEnvironment) -> List<V>
    ): Joiner<T> {
        return object : Joiner<T> {
            override fun apply(t: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder {
                return this@Joiner.apply(t).dataFetcher(name) {
                    GlobalScope.future {
                        f(it.getSource(), disallowGetSource(it))
                    }
                }

            }
        }
    }

    /**
     * We pass the DataFetchingEnvironment back to the caller, however it is
     * important that we do not allow getSource() as callers must use
     * the more strictly typed T in the callback
     */
    private fun disallowGetSource(dataFetchingEnvironment: DataFetchingEnvironment): DataFetchingEnvironment {
        return object : DelegatingDataFetchingEnvironment(dataFetchingEnvironment) {
            override fun <T : Any?> getSource(): T {
                throw Exception("Disallowed getSource access on joined data environment")
            }
        }
    }

    override fun apply(t: TypeRuntimeWiring.Builder): TypeRuntimeWiring.Builder {
        return t
    }
}