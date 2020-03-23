package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class SearchOperatorGreaterThanTest : SearchOperatorTest() {

    @Test
    fun `test gt float operator`() {

        //when
        val result = performGet("/testEntities?floatValue:gt=2.5")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())

    }

    @Test
    fun `test gt int operator`() {
        //when
        val result = performGet("/testEntities?child.num:gt=2")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
    }

    @Test
    fun `test gt list int operator`() {
        //when
        val result = performGet("/testEntities?childList.num:gt=3")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
    }
}
