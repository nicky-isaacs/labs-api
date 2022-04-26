package labs.nisaacs

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class LabsApiTest(private val application: EmbeddedApplication<*>, @Client("/") private val client: HttpClient) :
    StringSpec({



        "test the server is running" {
            assert(application.isRunning)
        }

        "test you can call the API" {
            val req: HttpRequest<String> = HttpRequest.POST(
                "/graphql",
                """{"query":"query {\n  user_by_email(email: \"asdf\"){\n    email\n    friend {\n      email\n      friend {\n        email\n        friend {\n          email\n          friend {\n            email\n          }\n        }\n      }\n    }\n  }\n}","variables":null}"""
            )

            val response = client.toBlocking().retrieve(req, String::class.java)
            response shouldBe "{\"data\":{\"user_by_email\":{\"email\":\"asdf\",\"friend\":{\"email\":\"friend+2@gmail.com\",\"friend\":{\"email\":\"friend+3@gmail.com\",\"friend\":{\"email\":\"friend+4@gmail.com\",\"friend\":{\"email\":\"friend+5@gmail.com\"}}}}}}}"
        }
    })
