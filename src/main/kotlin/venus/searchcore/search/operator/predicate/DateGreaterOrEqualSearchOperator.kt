package venus.searchcore.search.operator.predicate

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.DateTimePath
import org.springframework.stereotype.Service
import venus.searchcore.search.operator.localDateClass
import venus.searchcore.search.operator.localDateParser
import venus.searchcore.search.operator.localDateTimeClass
import venus.searchcore.search.operator.localDateTimeParser
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DateGreaterOrEqualSearchOperator : SearchOperator("dgoe") {
    @Suppress("UNCHECKED_CAST")
    override fun <T> getPredicate(booleanBuilder: BooleanBuilder, path: Path<T>, values: MutableList<String>) {

        when (path.type) {
            localDateClass ->
                booleanBuilder.and((path as DatePath<LocalDate>).goe(localDateParser.convert(values[0])))
            localDateTimeClass ->
                booleanBuilder.and((path as DateTimePath<LocalDateTime>).goe(localDateTimeParser.convert(values[0])))
        }
    }
}
