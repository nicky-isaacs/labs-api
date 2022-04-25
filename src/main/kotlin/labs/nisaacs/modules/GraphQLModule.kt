package labs.nisaacs.modules

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver
import jakarta.inject.Singleton
import labs.nisaacs.graphql.SchemaBuilder
import java.io.BufferedReader
import java.io.InputStreamReader

@Factory
class GraphQLModule {

    @Bean
    @Singleton
    fun graphQL(schemaBuilder: SchemaBuilder): GraphQL {
        return schemaBuilder.graphQL()
    }
}

