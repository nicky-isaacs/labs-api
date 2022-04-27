package labs.nisaacs

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.MySQLContainer.MYSQL_PORT

@MicronautTest
class LabsApiTest : AnnotationSpec(), TestPropertyProvider {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    val databaseName = "test_" + javaClass.simpleName
    val testDbContainer = TestDbContainer.instance
    val host = testDbContainer.host
    val jdbcURL =
        "jdbc:mysql://$host:${testDbContainer.getMappedPort(MYSQL_PORT)}/${databaseName}?createDatabaseIfNotExist=true"

    override fun getProperties(): Map<String, String> {
        return mapOf(
            "datasources.default.url" to jdbcURL,
            "datasources.default.password" to testDbContainer.password
        )
    }

    @Test
    fun test1() {
        val req: HttpRequest<String> = HttpRequest.POST(
            "/graphql",
            """{"query":"query {\n  user_by_email(email: \"asdf\"){\n    email\n    friend {\n      email\n      friend {\n        email\n        friend {\n          email\n          friend {\n            email\n          }\n        }\n      }\n    }\n  }\n}","variables":null}"""
        )

        val response = client.toBlocking().retrieve(req, String::class.java)
        response shouldBe "{\"data\":{\"user_by_email\":{\"email\":\"asdf\",\"friend\":{\"email\":\"friend+2@gmail.com\",\"friend\":{\"email\":\"friend+3@gmail.com\",\"friend\":{\"email\":\"friend+4@gmail.com\",\"friend\":{\"email\":\"friend+5@gmail.com\"}}}}}}}"
    }
}
