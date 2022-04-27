package labs.nisaacs

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.Micronaut.*
import jakarta.inject.Inject
import jakarta.inject.Singleton
import labs.nisaacs.dal.mysql.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("labs.nisaacs")
        .start()
}

@Singleton
class StartupHandler @Inject constructor(val database: Database) : ApplicationEventListener<StartupEvent> {
    override fun onApplicationEvent(event: StartupEvent?) {
        transaction(this.database) {
            SchemaUtils.create(UserTable)

            this.exec("SELECT DATABASE();", emptyList(), StatementType.SELECT) {
                printRs(it)
            }

            this.exec("SHOW TABLES;", emptyList(), StatementType.SELECT) {
                printRs(it)
            }
        }
    }

    fun printRs(r: ResultSet, i: Int = 1) {
        if (r.next()) {
            if (i > 1) print(",  ")
            val columnValue: String = r.getString(i)
            try {
                println(columnValue)
                printRs(r, i + 1)
            } catch (e: Exception) {

            }
        }
    }
}