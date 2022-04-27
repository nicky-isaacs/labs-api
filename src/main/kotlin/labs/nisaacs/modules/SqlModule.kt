package labs.nisaacs.modules

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import jakarta.inject.Inject
import jakarta.inject.Singleton
import labs.nisaacs.Logging
import org.jetbrains.exposed.sql.Database

@Singleton
open class SQLConfig @Inject constructor(
    @param:Property(name = "datasources.default.url")
    val jdbcURL: String,
    @param:Property(name = "datasources.default.password")
    val jdbcPassword: String
)

@Factory
class SqlModule @Inject constructor(val sqlConfig: SQLConfig) : Logging {

    @Singleton
    fun database(): Database {
        logger.info("the info: ${this.sqlConfig.jdbcURL}")
        return Database.connect(
            url = this.sqlConfig.jdbcURL,
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = this.sqlConfig.jdbcPassword,
            setupConnection = {
                it.createStatement().execute("CREATE DATABASE IF NOT EXISTS test_wat;")
            }
        )
    }


}