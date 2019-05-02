# Assumptions

Based on the requirements, the transactions are only needed to populate the statistics and nothing else,
so based on this I'm considering them as transient, and just using it to compose the statistics and discarding them.

This allow me to not have a `TransactionsService` to keep track of the transactions and a `StatisticsService` to aggregate the data.
This is good because it allowed me to have less code and a more direct and simple approach, but could also be considered bad
because of the `blurry separation or concerns`. But if decided to split the modules, we are not far from there.

#### `StatisticsService`
There I'm grouping the transactions by seconds, this way we can be sure that we are going to have at most 60 entries in our map.
This way is safe to assume that both the read/write operations are going to be O(60) -> O(1).
We could also have an fixed size array and loop through it, but in this case we would need to do the synchronization
manually.

I'm assuming that, when expiring transactions, we can expire all
transactions from that second, i.e., I am potentially expiring
transactions before I should because the milliseconds didn't expire yet.

If we need more precision, one possible way to increase the granularity by using more than just seconds to group the statistics.
This is going to increase the size of the map, but it would also continue "constant" over time.

Regarding the invalidation, I opted to invalidate statistics online,
reset during write and filtering out during reads,
this avoids a scheduled thread expiring data in a fixed delay fighting with the write/read operations to obtain locks.

#### `Tests`

I have written a couple of unit tests and some integration tests to complement the already existing integration tests.


Any further questions are very welcome.
