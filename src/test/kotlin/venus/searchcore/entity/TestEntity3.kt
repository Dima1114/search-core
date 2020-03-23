package venus.searchcore.entity

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class TestEntity3(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        var num: Int = 0,
        val date: LocalDate? = null,
        val type: TestEnum = TestEnum.KOTLIN,
        val name: String? = null)
