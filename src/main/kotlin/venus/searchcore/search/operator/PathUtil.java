package venus.searchcore.search.operator;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CollectionPathBase;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

public class PathUtil {

    public static Path<?> reifyPath(EntityPathResolver resolver, PropertyPath path, Optional<Path<?>> base) {

        Optional<Path<?>> map = base.filter(it -> it instanceof CollectionPathBase).map(CollectionPathBase.class::cast)//
                .map(CollectionPathBase::any)//
                .map(Path.class::cast)//
                .map(it -> reifyPath(resolver, path, Optional.of(it)));

        return map.orElseGet(() -> {

            Path<?> entityPath = base.orElseGet(() -> resolver.createPath(path.getOwningType().getType()));

            Field field = org.springframework.data.util.ReflectionUtils.findRequiredField(entityPath.getClass(),
                    path.getSegment());
            Object value = ReflectionUtils.getField(field, entityPath);

            PropertyPath next = path.next();

            if (next != null) {
                return reifyPath(resolver, next, Optional.of((Path<?>) value));
            }

            return (Path<?>) value;
        });
    }
}
