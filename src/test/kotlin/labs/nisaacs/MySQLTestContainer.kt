package labs.nisaacs

import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

fun image(): DockerImageName {
    return DockerImageName.parse("amd64/labs-api/fastmysql:8")
        .asCompatibleSubstituteFor("mysql:8")
}

class TestDbContainer : MySQLContainer<TestDbContainer>(image()) {
    companion object {
        val instance: TestDbContainer by lazy {
            TestDbContainer()
        }

        fun start() {
            if (!instance.isCreated) {
                instance.start() // At this point we have a running PostgreSQL instance as a Docker container
            }
        }

        fun stop() {
            instance.stop()
        }
    }
}