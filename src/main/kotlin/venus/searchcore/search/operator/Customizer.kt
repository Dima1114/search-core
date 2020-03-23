package venus.searchcore.search.operator

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.PathBuilder
import org.springframework.data.mapping.PropertyPath
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.data.querydsl.EntityPathResolver
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.util.TypeInformation
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import venus.searchcore.search.operator.predicate.SearchOperator
import java.util.*
import kotlin.collections.HashMap

class Customizer(private val resolver: EntityPathResolver,
                 private val searchPredicates: Map<String, SearchOperator>) {

    fun customizeQuery(params: MultiValueMap<String, String>,
                       type: TypeInformation<*>,
                       bindings: QuerydslBindings,
                       root: EntityPath<*>) {

        // Group params by field names
        val structuredParams = structureParameters(params)

        // Iterate over all the fields that take part in the filter
        structuredParams.forEach { (propertyPath, criteria) ->

            var pp: PropertyPath = getProperty(propertyPath, type) ?: return@forEach
            val path = reifyPath(resolver, pp, Optional.empty())

            var propertyName: String? = null
            while (pp.hasNext()) {
                propertyName = propertyName?.let { "$it." + pp.segment } ?: pp.segment
                pp = pp.next()!!
            }
            val rootBuilder = PathBuilder(root.type, root.metadata)
            val builder = propertyName?.let { PathBuilder(pp.owningType.type, rootBuilder.get(it).toString()) }
                    ?: rootBuilder

            handle(bindings, path, builder, getLastProperty(pp), criteria, searchPredicates)
        }
    }

    private fun getLastProperty(pp: PropertyPath): String {

        val arr = pp.toDotPath().split(".")
        return arr[arr.size - 1]
    }

    private fun structureParameters(params: MultiValueMap<String, String>): MutableMap<String, MultiValueMap<String, String>> {

        val structuredParams = HashMap<String, MultiValueMap<String, String>>()

        params.entries.forEach { (key, value) ->
            val operator = getEntityOperator(key)
            val propertyPath = if (DEFAULT_OPERATOR == operator) key else key.replace(":$operator", "")

            // Get a group of operators for a particular field
            val group: MultiValueMap<String, String> = structuredParams.getOrPut(propertyPath) { LinkedMultiValueMap() }

            // Put an operator into it's group
            group[key] = value
        }

        return structuredParams
    }

    private fun getProperty(propertyPath: String, type: TypeInformation<*>): PropertyPath? {
        if (RESERVED_PARAMS.contains(propertyPath)) {
            return null
        }
        //Check valid path
        return try {
            PropertyPath.from(propertyPath, type)
        } catch (e: PropertyReferenceException) {
            null
        }

    }

    private fun getEntityOperator(path: String): String? {
        val colon = ":"
        return if (!path.contains(colon)) DEFAULT_OPERATOR else path.split(colon.toRegex()).component2()
    }

    private fun <T> handle(bindings: QuerydslBindings,
                           path: Path<T>,
                           pathBuilder: PathBuilder<*>,
                           pathString: String,
                           map: MultiValueMap<String, String>,
                           searchPredicates: Map<String, SearchOperator>) {

        val booleanBuilder = BooleanBuilder()
        val alias = map.keys.lastOrNull() ?: pathString

        // Loop by the criteria for the particular field
        map.filter { (_, value) -> value.isNotEmpty() }
                .forEach { (key, value) ->

                    searchPredicates[getEntityOperator(key)]
                            ?.let {
                                it.getPredicate(booleanBuilder, path, value)
                                clearNullable(value)
                            }
                            ?: bindings.bind(pathBuilder.get(pathString)).withDefaultBinding()
                }
        // Apply criteria for path
        booleanBuilder.value?.let {
            bindings.bind(pathBuilder.get(pathString)).`as`(alias).all { _, _ -> Optional.of(it) }
        }
    }

    private fun clearNullable(list: MutableList<String>) {
        if (list.filterNotNull().isEmpty()) list.clear()
    }


}



