package venus.searchcore.search.operator

import venus.searchcore.converter.LocalDateCustomConverter
import venus.searchcore.converter.LocalDateTimeCustomConverter
import java.time.LocalDate
import java.time.LocalDateTime

val longClass = java.lang.Long::class.java
private val integerClass = java.lang.Integer::class.java
val doubleClass = java.lang.Double::class.java
private val floatClass = java.lang.Float::class.java
val localDateClass = LocalDate::class.java
val localDateTimeClass = LocalDateTime::class.java

val longs = listOf(longClass, Long::class.java, integerClass, Int::class.java)
val ints = listOf(integerClass, Int::class.java)
val doubles = listOf(doubleClass, Double::class.java, floatClass, Float::class.java)
val floats = listOf(floatClass, Float::class.java)

val localDateParser = LocalDateCustomConverter()
val localDateTimeParser = LocalDateTimeCustomConverter()

val RESERVED_PARAMS = arrayListOf("projection", "size", "page", "sort", "all_in_one_page")
const val DEFAULT_OPERATOR = "default"
