package venus.searchcore.search.operator

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should not be`
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import venus.searchcore.entity.TestEntity
import venus.searchcore.entity.TestEntity2
import venus.searchcore.entity.TestEntity3
import venus.searchcore.entity.TestEnum
import venus.searchcore.integration.AbstractTestMvcIntegration
import venus.searchcore.repository.TestEntity2Repository
import venus.searchcore.repository.TestEntityRepository
import java.time.LocalDate
import java.time.LocalDateTime

class SearchOperatorTest : AbstractTestMvcIntegration() {

    @Autowired
    private lateinit var testEntityRepository: TestEntityRepository

    @Autowired
    private lateinit var testEntity2Repository: TestEntity2Repository

    companion object {

        val testEntity = TestEntity(name = "test_first", floatValue = 1.5F)
        val testEntity2 = TestEntity(date = LocalDate.now().plusDays(1), name = "test_second", floatValue = 2.5F)
        val testEntity3 = TestEntity(date = LocalDate.now().plusDays(2), name = "test_third", floatValue = 3.5F)
        val testEntity4 = TestEntity(date = LocalDate.now().plusDays(3), name = "fourth")

        val entity2Child1 = TestEntity2(date = LocalDateTime.now().minusDays(1).withNano(0), type = TestEnum.JAVA)
        val entity2child2 = TestEntity2(date = LocalDateTime.now().minusDays(2).withNano(0), num = 2, name = "child_two")
        val entity2child3 = TestEntity2(date = LocalDateTime.now().plusDays(3).withNano(0), num = 3, name = "child_three")
//        val child4 = TestEntity2(parent = testEntity4, date = LocalDate.now().plusDays(4), num = 4)

        val entity3Child1 = TestEntity3(name = "list_0", num = 0, date = LocalDate.now().minusDays(1), type = TestEnum.JAVA)
        val entity3Child2 = TestEntity3(name = "list_1", num = 1, date = LocalDate.now())
        val entity3Child3 = TestEntity3(name = "list_2", num = 2, date = LocalDate.now().plusDays(1))
        val entity3Child4 = TestEntity3(name = "list_3", num = 3, date = LocalDate.now().plusDays(2))
        val entity3Child5 = TestEntity3(name = "list_4", num = 4, date = LocalDate.now().plusDays(3))
    }

    @Before
    fun setUp(){
        testEntity.apply {
            child = entity2Child1
            childList = mutableListOf(entity3Child1, entity3Child2)
        }
        testEntity2.apply {
            child = entity2child2
            childList = mutableListOf(entity3Child3, entity3Child4)
        }
        testEntity3.apply {
            child = entity2child3
            childList = mutableListOf(entity3Child5)
        }

        testEntityRepository.saveAll(mutableListOf(testEntity, testEntity2, testEntity3, testEntity4))
    }

    @Test
    fun `test repositories data`(){

        //when
        val entities = testEntityRepository.findAll()

        //then
        entities.size `should be equal to` 4
        entities[0].child `should not be` null
        entities[0].childList.size `should be` 2
        entities[1].child `should not be` null
        entities[1].childList.size `should be` 2
        entities[2].child `should not be` null
        entities[2].childList.size `should be` 1
        entities[3].child `should be` null
        entities[3].childList.size `should be` 0
    }

    @Test
    fun `test dgoe and dloe operator`() {

        //given
        val dateFrom = LocalDate.now().plusDays(1)
        val dateTo = LocalDate.now().plusDays(2)

        //when
        val result = performGet("/testEntities?date:dgoe=$dateFrom&date:dloe=$dateTo")
        //then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
    }

    @Test
    fun `test sort operator`() {
        //when
        var result = performGet("/testEntities?sort=date,desc")
        //then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$._embedded.testEntities[0].name").value(testEntity4.name!!))
                .andExpect(jsonPath("$._embedded.testEntities[1].name").value(testEntity3.name!!))
                .andExpect(jsonPath("$._embedded.testEntities[2].name").value(testEntity2.name!!))
                .andExpect(jsonPath("$._embedded.testEntities[3].name").value(testEntity.name!!))

        //when
        result = performGet("/testEntities?sort=floatValue,desc")
        //then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$._embedded.testEntities[0].name").value(testEntity3.name))
                .andExpect(jsonPath("$._embedded.testEntities[1].name").value(testEntity2.name))
                .andExpect(jsonPath("$._embedded.testEntities[2].name").value(testEntity.name))
                .andExpect(jsonPath("$._embedded.testEntities[3].name").value(testEntity4.name))

        //when
        result = performGet("/testEntities?sort=child.num,asc")
        //then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$._embedded.testEntities[0].name").value(testEntity4.name))
                .andExpect(jsonPath("$._embedded.testEntities[1].name").value(testEntity.name))
                .andExpect(jsonPath("$._embedded.testEntities[2].name").value(testEntity2.name))
                .andExpect(jsonPath("$._embedded.testEntities[3].name").value(testEntity3.name))
    }

    @Test
    fun `test page and size operator`() {
        //when
        var result = performGet("/testEntities?size=2&page=1&sort=id,asc")
        //then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(2))
                .andExpect(jsonPath("$.page.number").value(1))
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity4.name}')]").exists())

        //when
        result = performGet("/testEntities?size=3&page=0&sort=id,asc")
        //then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$.page.size").value(3))
                .andExpect(jsonPath("$.page.totalPages").value(2))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
    }

    @Test
    fun `test all in one page operator`() {
        //when
        val result = performGet("/testEntities?sort=id,asc")
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$.page.size").value(1000))
                .andExpect(jsonPath("$.page.totalPages").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
                .andExpect(jsonPath("$._embedded.testEntities[?(@.name == '${testEntity4.name}')]").exists())
    }

    protected fun performGet(query: String): ResultActions {
        return mvc.perform(get(query).accept(MediaType.APPLICATION_JSON))
    }
}
