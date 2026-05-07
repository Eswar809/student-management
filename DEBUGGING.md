# Debugging N+1 Queries
Initially, listing 10 courses with 50 students each resulted in 1 + 10 queries.
By adding a JOIN FETCH in the CourseRepository, we reduced the query count from 1+N to 1.
See the test CourseRepositoryTest.findAllActiveCoursesWithStudents_avoids_n_plus_1 which asserts the Hibernate PrepareStatementCount drops to 1.
