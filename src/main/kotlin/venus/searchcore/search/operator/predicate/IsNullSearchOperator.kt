package venus.searchcore.search.operator.predicate

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.SimpleExpression
import org.springframework.stereotype.Service

@Service
class IsNullSearchOperator : SearchOperator("isNull") {
    override fun <T> getPredicate(booleanBuilder: BooleanBuilder, path: Path<T>, values: MutableList<String>) {
        booleanBuilder.and((path as SimpleExpression<*>).isNull)
    }
}
