package venus.searchcore.search.operator

import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.CollectionPathBase
import org.springframework.data.mapping.PropertyPath
import org.springframework.data.querydsl.EntityPathResolver
import org.springframework.data.util.ReflectionUtils
import java.util.*


fun reifyPath(resolver: EntityPathResolver, path: PropertyPath, base: Optional<Path<*>>): Path<*> {

    val map: Optional<Path<*>> = base
            .filter { it is CollectionPathBase<*, *, *> }
            .map { obj -> obj as CollectionPathBase<*, *, *> } //
            .map { obj -> obj.any() } //
            .map { obj -> obj as Path<*> } //
            .map { reifyPath(resolver, path, Optional.of(it)) }

    return map.orElseGet { getPath(resolver, path, base) }
}

private fun getPath(resolver: EntityPathResolver, path: PropertyPath, base: Optional<Path<*>>): Path<*> {

    val entityPath = base.orElseGet { resolver.createPath(path.owningType.type) }

    val field = ReflectionUtils.findRequiredField(entityPath.javaClass,
            path.segment)
    val value = org.springframework.util.ReflectionUtils.getField(field, entityPath)

    val next = path.next()

    return next?.let {
        reifyPath(resolver, next, Optional.of((value as Path<*>)))
    } ?: value as Path<*>
}

