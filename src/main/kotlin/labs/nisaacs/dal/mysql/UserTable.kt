package labs.nisaacs.dal.mysql

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable() {
    val name = varchar("name", 50)
}