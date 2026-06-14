package dev.timjelenz.openlocationapi.jpafunctions;

import java.util.List;
import java.util.function.Supplier;

import org.hibernate.metamodel.mapping.BasicValuedMapping;
import org.hibernate.query.sqm.function.FunctionKind;
import org.hibernate.query.sqm.function.PatternBasedSqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.ArgumentsValidator;
import org.hibernate.query.sqm.produce.function.FunctionReturnTypeResolver;
import org.hibernate.query.sqm.produce.function.internal.PatternRenderer;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.sql.ast.tree.SqlAstNode;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * Basic ReturnTypeResolver.
 */
class ReturnTypeResolver implements FunctionReturnTypeResolver {

    @Override
    public BasicValuedMapping resolveFunctionReturnType(
        final Supplier<BasicValuedMapping> supplier,
        final List<? extends SqlAstNode> arguments
    ) {
        return supplier.get();
    }
}

/**
 * Basic ArgumentsValidator, allows only numeric types.
 */
class HaversineArgumentsValidator implements ArgumentsValidator {

    private boolean isNumeric(final String typeName) {

        return switch (typeName.toLowerCase()) {
            case "integer", "int", "long", "double", "bigdecimal", "numeric", "decimal" -> true;
            default -> false;
        };
    }

    @Override
    public void validate(
        final List<? extends SqmTypedNode<?>> arguments,
        final String functionName,
        final TypeConfiguration typeConfiguration
    ) {
        if (arguments.size() != 4) {
            throw new IllegalArgumentException("Number of arguments must be 2");
        }
        for (int i = 0; i <= 4; i++) {
            if (
                !isNumeric(
                    arguments.get(i)
                        .getNodeType()
                        .getTypeName()
                )
            ) {
                throw new IllegalArgumentException("Arguments can only");
            }
        }
    }
}

/**
 * HaversineFunction descriptor. Instead of using th
 * Optionally, instead of `HAVERSINEFORMULAPATTERN`, you can also define
 * a `haversine` function in the db and hibernate will use it
 */
public class HaversineFunctionDescriptor extends PatternBasedSqmFunctionDescriptor {

    private static final String HAVERSINEFORMULAPATTERN = """
        2 * 6371 * ASIN(
            SQRT(
                POWER(SIN(RADIANS(?1 - ?3)  / 2), 2) +
                COS(RADIANS(?3)) * COS(RADIANS(?1)) *
                POWER(SIN(RADIANS(?2 - ?4) / 2), 2)
            )
        )
    """;

    public HaversineFunctionDescriptor() {
        super(
            new PatternRenderer(HAVERSINEFORMULAPATTERN),
            new HaversineArgumentsValidator(),
            new ReturnTypeResolver(),
            null,
            "haversine",
            FunctionKind.NORMAL,
            null
        );
    }
}
