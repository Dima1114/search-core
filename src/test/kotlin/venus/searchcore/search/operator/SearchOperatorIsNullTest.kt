package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class SearchOperatorIsNullTest : SearchOperatorTest() {

    @Test
    fun `test child isNull operator`() {

        //when
        val result = performGet("/testEntities?child:isNull")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity4.name}')]").exists())

    }

    @Test
    fun `test date isNull operator`() {
        //when
        val result = performGet("/testEntities?date:isNull")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())

    }

    @Test
    fun `test float isNull operator`() {
        //when
        val result = performGet("/testEntities?floatValue:isNull")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity4.name}')]").exists())
    }

    @Test
    fun `test child string isNull operator`() {
        //when
        val result = performGet("/testEntities?child.name:isNull")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
    }
}
