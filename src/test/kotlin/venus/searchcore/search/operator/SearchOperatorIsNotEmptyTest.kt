package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class SearchOperatorIsNotEmptyTest : SearchOperatorTest() {

    @Test
    fun `test child list not empty operator`() {

        //when
        val result = performGet("/testEntities?childList:notEmpty")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
    }
}
