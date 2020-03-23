package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.LocalDateTime

class SearchOperatorDateGreaterOrEqualsTest : SearchOperatorTest() {

    @Test
    fun `test dgoe operator`() {

        //given
        val date = LocalDate.now().plusDays(3)

        //when
        val result = performGet("/testEntities?date:dgoe=$date")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity4.name}')]").exists())
    }

    @Test
    fun `test child property dgoe operator`() {
        //given
        val dateTime = LocalDateTime.now().withNano(0)

        //when
        val result = performGet("/testEntities?child.date:dgoe=$dateTime")
        //then
        result
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
    }

    @Test
    fun `test child list property dgoe operator`() {
        //given
        val date = LocalDate.now().plusDays(3)

        //when
        val result = performGet("/testEntities?childList.date:dgoe=$date")
        //then
        result
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity3.name}')]").exists())
    }
}
