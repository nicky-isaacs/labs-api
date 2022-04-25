package labs.nisaacs.graphql.schema

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture


object Fetcher {

    fun <T> build(
        scope: CoroutineScope = GlobalScope,
        f: suspend CoroutineScope.(environment: DataFetchingEnvironment) -> T
    ): DataFetcher<CompletableFuture<T>> {

        return DataFetcher { env ->
            scope.future {
                f(env)
            }
        }
    }

}