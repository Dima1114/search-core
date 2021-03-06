package venus.searchcore.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "testEntity2")
class TestEntity2(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        val name: String? = null,
        var num: Int = 0,
        val date: LocalDateTime? = null,
        val type: TestEnum = TestEnum.KOTLIN)
