package venus.searchcore.search.operator

import org.junit.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchOperatorDateEqualsTest : SearchOperatorTest() {

    @Test
    fun `test deq operator`() {

        //given
        val date = LocalDate.now().plusDays(3)

        //when
        val result = performGet("/testEntities?date:deq=${date!!.format(DateTimeFormatter.ofPattern(DATE_FORMAT))}")
        //then
        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity4.name}')]").exists())

    }

    @Test
    fun `test child property deq operator`() {

        //given
        val dateTime = testEntity.child!!.date

        //when
        val result = performGet("/testEntities?child.date:deq=$dateTime")
        //then
        result
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
    }

    @Test
    fun `test child list property deq operator`() {

        //given
        val date = testEntity.childList[0].date

        //when
        val result = performGet("/testEntities?childList.date:deq=${date!!.format(DateTimeFormatter.ofPattern(DATE_FORMAT))}")
        //then
        result
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.testEntities[?(@.name == '${testEntity.name}')]").exists())
    }
}
