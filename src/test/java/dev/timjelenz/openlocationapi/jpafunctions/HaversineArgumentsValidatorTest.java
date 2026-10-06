package dev.timjelenz.openlocationapi.jpafunctions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hibernate.query.sqm.SqmExpressible;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.spi.TypeConfiguration;
import org.junit.jupiter.api.Test;

class HaversineArgumentsValidatorTest {

    private final HaversineArgumentsValidator validator =
        new HaversineArgumentsValidator();
    private final TypeConfiguration typeConfiguration = new TypeConfiguration();

    @Test
    void acceptsFullyQualifiedNumericTypeNames() {
        final List<? extends SqmTypedNode<?>> arguments = List.of(
            argument("java.math.BigDecimal"),
            argument("java.lang.Double"),
            argument("java.lang.Integer"),
            argument("java.lang.Long")
        );

        assertDoesNotThrow(
            () -> validator.validate(arguments, "haversine", typeConfiguration)
        );
    }

    @Test
    void acceptsHibernateNumericTypeNames() {
        final List<? extends SqmTypedNode<?>> arguments = List.of(
            argument("big_decimal"),
            argument("double"),
            argument("integer"),
            argument("long")
        );

        assertDoesNotThrow(
            () -> validator.validate(arguments, "haversine", typeConfiguration)
        );
    }

    @Test
    void rejectsWrongArgumentCount() {
        final List<? extends SqmTypedNode<?>> arguments = List.of(
            argument("java.math.BigDecimal")
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(arguments, "haversine", typeConfiguration)
        );
    }

    @Test
    void rejectsNonNumericArguments() {
        final List<? extends SqmTypedNode<?>> arguments = List.of(
            argument("java.lang.String"),
            argument("java.lang.String"),
            argument("java.lang.String"),
            argument("java.lang.String")
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(arguments, "haversine", typeConfiguration)
        );
    }

    private static SqmTypedNode<?> argument(final String typeName) {
        final SqmExpressible<?> expressible = mock(SqmExpressible.class);
        when(expressible.getTypeName()).thenReturn(typeName);

        final SqmTypedNode<?> node = mock(SqmTypedNode.class);
        when(node.getNodeType()).thenReturn((SqmExpressible) expressible);

        return node;
    }
}
