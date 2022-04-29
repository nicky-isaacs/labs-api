package labs.nisaacs.modules

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import jakarta.inject.Inject
import jakarta.inject.Singleton
import labs.nisaacs.Logging
import org.jetbrains.exposed.sql.Database
import javax.validation.constraints.NotBlank


@ConfigurationProperties("datasources.default")
class SQLConfig {

    @NotBlank
    lateinit var url: String

    @NotBlank
    lateinit var password: String

}



@Factory
class SqlModule @Inject constructor(val sqlConfig: SQLConfig) : Logging {

    @Singleton
    fun database(): Database {
        logger.info("the info: ${this.sqlConfig.url}")
        return Database.connect(
            url = this.sqlConfig.url,
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = this.sqlConfig.password,
            setupConnection = {
                it.createStatement().execute("CREATE DATABASE IF NOT EXISTS test_wat;")
            }
        )
    }


}