package venus.searchcore.entity

import venus.searchcore.validation.Unique
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
@Unique(fields = ["name"], fieldsBunch = ["date", "floatValue"], errorFields = ["floatValue"])
class TestEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        val name: String? = null,
        val date: LocalDate? = null,
        val floatValue: Float? = null,
        @ManyToOne(cascade = [CascadeType.ALL])
        var child: TestEntity2? = null,

        @OneToMany(cascade = [CascadeType.ALL])
        @JoinTable(name = "entity1_entity3",
                joinColumns = [JoinColumn(name = "id_1")],
                inverseJoinColumns = [JoinColumn(name = "id_3")])
        var childList: MutableList<TestEntity3> = mutableListOf()

)
