package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.LocalDateTime

class SearchOperatorDateLessOrEqualsTest : SearchOperatorTest() {

    @Test
    fun `test dloe operator`() {

        //given
        val date = LocalDate.now().plusDays(1)

        //when
        val result = performGet("/testEntities?date:dloe=$date")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())

    }

    @Test
    fun `test child property dloe operator`() {

        val dateTime = LocalDateTime.now().minusDays(2).withNano(0)

        //when
        val result = performGet("/testEntities?child.date:dloe=$dateTime")
        //then
        result
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity2.name}')]").exists())
    }

    @Test
    fun `test child list property dloe operator`() {

        val date = LocalDate.now().minusDays(1)

        //when
        val result = performGet("/testEntities?childList.date:dloe=$date")
        //then
        result
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
    }

}
