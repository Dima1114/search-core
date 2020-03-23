package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class SearchOperatorIsEmptyTest : SearchOperatorTest() {

    @Test
    fun `test child list empty operator`() {

        //when
        val result = performGet("/testEntities?childList:empty")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity4.name}')]").exists())

    }
}
