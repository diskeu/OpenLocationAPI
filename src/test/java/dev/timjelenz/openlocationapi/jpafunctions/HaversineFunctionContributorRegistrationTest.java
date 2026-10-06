package dev.timjelenz.openlocationapi.jpafunctions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ServiceLoader;

import org.hibernate.boot.model.FunctionContributor;
import org.junit.jupiter.api.Test;

class HaversineFunctionContributorRegistrationTest {

    @Test
    void contributorIsRegisteredThroughHibernateSpi() {
        final boolean registered = ServiceLoader.load(FunctionContributor.class)
            .stream()
            .map(ServiceLoader.Provider::type)
            .anyMatch(HaversineFunctionContributer.class::equals);

        assertTrue(
            registered,
            "HaversineFunctionContributer must be registered in "
                + "META-INF/services/org.hibernate.boot.model.FunctionContributor"
        );
    }
}
