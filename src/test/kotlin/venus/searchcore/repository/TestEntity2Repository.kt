package venus.searchcore.repository

import venus.searchcore.entity.TestEntity
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface TestEntity2Repository : TestBaseRepository<TestEntity>