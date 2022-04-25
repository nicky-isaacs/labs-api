package labs.nisaacs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Logging {
    val logger: Logger
        get() {
            return LoggerFactory.getLogger(this.javaClass)
        }
}