package labs.nisaacs.graphql.schema

interface Bookssssss {
    fun id(): String
    fun title(): String
    fun author(): String
}

interface Library {

    /**
     * Assume we store Books IDs in our backing store. We could imagine
     * a system by which given a function from Long => CompletableFuture<Book?> we
     * could automatically join these fields
     *
     * More generically if we have some object D and key type K,
     * and the ability to join data via the availability of some
     * function (K => CompletableFuture<D?>), it is nice to abstract the method
     * of object fetching away from the reference provider (in our case a Book ID)
     */
    fun books(): List<Book>

}