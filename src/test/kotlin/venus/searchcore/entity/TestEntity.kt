package venus.searchcore.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
class TestEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        val name: String? = null,
        val date: LocalDate? = null,
        val float: Float? = null,
        @ManyToOne(cascade = [CascadeType.ALL])
        var child: TestEntity2? = null)