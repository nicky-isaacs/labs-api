package labs.nisaacs

import kotlinx.coroutines.sync.Mutex
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName
import java.util.concurrent.CountDownLatch

fun image(): DockerImageName {
    return DockerImageName.parse("amd64/labs-api/fastmysql:8")
        .asCompatibleSubstituteFor("mysql:8")
}

class TestDbContainer : MySQLContainer<TestDbContainer>(image()) {
    companion object {
        val mut = Mutex()
        val latch = CountDownLatch(1)
        val instance: TestDbContainer by lazy {
            TestDbContainer()
        }

        /**
         * Attempts to start the server. If another thread is already starting, awaits for
         * start to complete
         *
         * An exception encountered during start will only affect the lock holding thread, other threads will become
         * unblocked but the container may be in a bad state.
         */
        fun start() {
            val locked = mut.tryLock()
            try {
                if (!instance.isCreated && locked) {
                    instance.start()
                }
            } finally {
                if (locked) {
                    mut.unlock()
                    latch.countDown()
                }
            }

            latch.await()
        }

        fun stop() {
            instance.stop()
        }

        fun <T : Any> T.jdbcURL(testDbContainer: TestDbContainer = instance): String {
            val databseName = "test_${this.javaClass.simpleName}"
            return "jdbc:mysql://${testDbContainer.host}:${testDbContainer.getMappedPort(MYSQL_PORT)}/${databseName}?createDatabaseIfNotExist=true"
        }

    }
}