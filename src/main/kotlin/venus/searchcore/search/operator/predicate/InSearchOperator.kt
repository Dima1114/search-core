package venus.searchcore.search.operator.predicate

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.EnumPath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.stereotype.Service
import venus.searchcore.search.operator.doubles
import venus.searchcore.search.operator.floats
import venus.searchcore.search.operator.ints
import venus.searchcore.search.operator.longs

@Service
class InSearchOperator : SearchOperator("in") {

    @Suppress("UNCHECKED_CAST")
    override fun <T> getPredicate(booleanBuilder: BooleanBuilder,
                                  path: Path<T>,
                                  values: MutableList<String>) {
        when {
            path is EnumPath -> {
                val conversionService = DefaultConversionService()
                val list = values.map { conversionService.convert(it, path.type) }.toList()
                booleanBuilder.and((path).`in`(list))
            }
            path.type in ints -> booleanBuilder.and((path as NumberPath<Int>)
                    .`in`(*values.map { it.toInt() }.toTypedArray()))
            path.type in longs -> booleanBuilder.and((path as NumberPath<Long>)
                    .`in`(*values.map { it.toLong() }.toTypedArray()))
            path.type in floats -> booleanBuilder.and((path as NumberPath<Int>)
                    .`in`(*values.map { it.toFloat() }.toTypedArray()))
            path.type in doubles -> booleanBuilder.and((path as NumberPath<Int>)
                    .`in`(*values.map { it.toDouble() }.toTypedArray()))
            else -> booleanBuilder.and((path as StringPath).`in`(*values.toTypedArray()))
        }
    }
}
