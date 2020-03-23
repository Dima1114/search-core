package venus.searchcore.search.operator.predicate

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.StringPath
import org.springframework.stereotype.Service

@Service
class ContainsSearchOperator : SearchOperator("contains") {
    override fun <T> getPredicate(booleanBuilder: BooleanBuilder, path: Path<T>, values: MutableList<String>) {
        values.forEach { booleanBuilder.and((path as StringPath).containsIgnoreCase(it)) }
    }
}
