package dev.timjelenz.openlocationapi.jpafunctions;

import java.util.List;
import java.util.Locale;

import org.hibernate.query.sqm.function.FunctionKind;
import org.hibernate.query.sqm.function.PatternBasedSqmFunctionDescriptor;
import org.hibernate.query.sqm.produce.function.ArgumentsValidator;
import org.hibernate.query.sqm.produce.function.StandardFunctionReturnTypeResolvers;
import org.hibernate.query.sqm.produce.function.internal.PatternRenderer;
import org.hibernate.query.sqm.tree.SqmTypedNode;
import org.hibernate.type.spi.TypeConfiguration;

/**
 * Basic ArgumentsValidator, allows only numeric types.
 */
class HaversineArgumentsValidator implements ArgumentsValidator {

    private boolean isNumeric(final String typeName) {
        if (typeName == null) {
            return true;
        }

        final String simpleName = typeName
            .substring(typeName.lastIndexOf('.') + 1)
            .replace("_", "")
            .toLowerCase(Locale.ROOT);

        return switch (simpleName) {
            case "byte", "short", "integer", "int", "long", "float", "double",
                "bigdecimal", "biginteger", "numeric", "decimal", "real" -> true;
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
            throw new IllegalArgumentException("Number of arguments must be 4");
        }
        for (int i = 0; i < arguments.size(); i++) {
            final var nodeType = arguments.get(i).getNodeType();
            final String typeName = nodeType != null
                ? nodeType.getTypeName()
                : null;
            if (!isNumeric(typeName)) {
                throw new IllegalArgumentException(
                    "Arguments must be numeric, got: " + typeName
                );
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
            StandardFunctionReturnTypeResolvers.useArgType(1),
            null,
            "haversine",
            FunctionKind.NORMAL,
            null
        );
    }
}
