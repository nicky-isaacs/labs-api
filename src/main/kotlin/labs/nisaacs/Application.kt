package labs.nisaacs

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("labs.nisaacs")
		.start()
}

