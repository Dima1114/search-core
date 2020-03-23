package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class SearchOperatorLessThanTest : SearchOperatorTest() {

    @Test
    fun `test lt float operator`() {

        //when
        val result = performGet("/testEntities?floatValue:lt=2.5")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())

    }

    @Test
    fun `test lt int operator`() {
        //when
        val result = performGet("/testEntities?child.num:lt=3")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())
    }

    @Test
    fun `test list lt int operator`() {
        //when
        val result = performGet("/testEntities?childList.num:lt=3")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())
    }
}
