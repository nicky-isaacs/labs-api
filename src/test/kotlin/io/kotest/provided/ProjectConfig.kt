package io.kotest.provided

import io.kotest.core.config.AbstractProjectConfig
import io.micronaut.test.extensions.kotest.MicronautKotestExtension
import labs.nisaacs.TestDbContainer

object ProjectConfig : AbstractProjectConfig() {
    override fun listeners() = listOf(MicronautKotestExtension)
    override val parallelism: Int
        get() = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1)

    override fun extensions() = listOf(MicronautKotestExtension)
    override fun beforeAll() {
        TestDbContainer.start()
    }

    override fun afterAll() {
        TestDbContainer.stop()
    }
}
