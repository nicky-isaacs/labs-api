package labs.nisaacs.graphql

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import io.micronaut.core.io.ResourceResolver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import labs.nisaacs.Logging
import labs.nisaacs.graphql.schema.Book
import labs.nisaacs.graphql.schema.User
import labs.nisaacs.graphql.schema.joiner
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.CompletableFuture

@Singleton
class SchemaBuilder @Inject constructor(private val resourceResolver: ResourceResolver) : Logging {


    fun graphQL(): GraphQL {
        val schemaGenerator = SchemaGenerator()

        // Create the runtime wiring.
        val runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query") { typeWiring ->
                typeWiring.dataFetcher("user_by_email") {
                    CompletableFuture.completedFuture(
                        User(1, it.getArgument("email"))
                    )
                }
            }.type("User",
                joiner<User>().join(
                    "friend"
                ) { user, _ ->
                    val newID = user.id + 1
                    user.copy(id = newID, email = "friend+${newID}@gmail.com")
                }.joinList("favorite_books") { user, _ ->
                    listOf(
                        Book(1, "${user.id} Fav Book!"),
                        Book(2, "${user.id} Fav Book 2!"),
                    )
                }).build()

        val schemaParser = SchemaParser()

        val typeRegistry = TypeDefinitionRegistry();
        typeRegistry.merge(
            schemaParser.parse(
                BufferedReader(
                    InputStreamReader(
                        resourceResolver.getResourceAsStream("classpath:schema.graphqls").get()
                    )
                )
            )
        )

        val graphQLSchema = schemaGenerator.makeExecutableSchema(
            typeRegistry,
            runtimeWiring
        )

        return GraphQL.newGraphQL(graphQLSchema).build()
    }


}