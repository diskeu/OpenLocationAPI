package dev.timjelenz.openlocationapi.jpafunctions;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;

/**
 * Contributes the haversine function.
 */
public class HaversineFunctionContributer implements FunctionContributor {

    @Override
    public void contributeFunctions(final FunctionContributions functionContributions) {
        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
        
        functionRegistry.register(
            "haversine",
            new HaversineFunctionDescriptor()
        );
    }
}
