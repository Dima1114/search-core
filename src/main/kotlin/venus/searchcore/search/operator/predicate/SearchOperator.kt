package venus.searchcore.search.operator.predicate

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Path

abstract class SearchOperator(val operator: String) {

    abstract fun <T> getPredicate(booleanBuilder: BooleanBuilder,
                                  path: Path<T>,
                                  values: MutableList<String>)
}
