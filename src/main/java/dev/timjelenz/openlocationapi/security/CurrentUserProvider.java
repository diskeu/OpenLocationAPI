package dev.timjelenz.openlocationapi.security;

import dev.timjelenz.openlocationapi.models.User;

public interface CurrentUserProvider {
    /**
     * Gets the current user.
     * 
     * @return the current user
     */
    User get();
}
