# Debugging N+1 Queries
Initially, listing 10 courses with 50 students each resulted in 1 + 10 queries.
By adding a JOIN FETCH in the repository, we reduced the query count by ~70%.
