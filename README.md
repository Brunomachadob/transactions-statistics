# Assumptions

#### `TransactionsService`
There I'm grouping the transactions by seconds, this way we can be sure that we are going to have at most 60 entries in our map.
This way is safe to assume that both the read/write operations are going to be O(60) -> O(1).
We could also have an fixed size array and loop through it, but in this case we would need to do the synchronization
manually.

I'm assuming that, when expiring transactions, we can expire all
transactions from that second, i.e., I am potentially expiring
transactions before I should because the milliseconds didn't expire yet.

If we need more precision, one possible way to fix this is grouping statistics by millis,
then having at most 60000 entries in the map. This way we could expire data by millis.

Regarding the invalidation, I opted to invalidate statistics online,
reset during write and filtered out during reads,
this avoids a scheduled thread expiring data in a fixed delay fighting with the write/read operations to obtain locks.


#### `Code duplication`

Because of this approach used, I duplicated a little bit of code (TransactionService/Statistics)

#### `Tests`

I have written a couple of unit tests and some integration tests to complement the already existing integration tests
