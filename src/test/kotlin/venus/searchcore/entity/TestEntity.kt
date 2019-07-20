package venus.searchcore.entity

import venus.searchcore.validation.Unique
import java.time.LocalDate
import javax.persistence.*

@Entity
@Unique(fields = ["name"], fieldsBunch = ["date", "float"], errorFields = ["float"])
class TestEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        val name: String? = null,
        val date: LocalDate? = null,
        val float: Float? = null,
        @ManyToOne(cascade = [CascadeType.ALL])
        var child: TestEntity2? = null)