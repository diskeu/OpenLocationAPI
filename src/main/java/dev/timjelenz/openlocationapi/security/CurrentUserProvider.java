package dev.timjelenz.openlocationapi.security;

import dev.timjelenz.openlocationapi.models.User;

public interface CurrentUserProvider {
    User get();
}
