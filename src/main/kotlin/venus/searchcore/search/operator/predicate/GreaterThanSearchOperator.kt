package venus.searchcore.search.operator.predicate

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.NumberPath
import org.springframework.stereotype.Service
import venus.searchcore.search.operator.doubles
import venus.searchcore.search.operator.longs

@Service
class GreaterThanSearchOperator : SearchOperator("gt") {

    override fun <T> getPredicate(booleanBuilder: BooleanBuilder, path: Path<T>, values: MutableList<String>) {
        when (path.type) {
            in longs -> booleanBuilder.and((path as NumberPath<*>).gt(values[0].toLong()))
            in doubles -> booleanBuilder.and((path as NumberPath<*>).gt(values[0].toDouble()))
        }
    }
}
