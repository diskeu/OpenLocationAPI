package dev.timjelenz.openlocationapi.dto.responses.user;

import dev.timjelenz.openlocationapi.models.User;

public record PrivateUserResponse(
    int id,
    String userName,
    String userEmail
) {
    public PrivateUserResponse(User user) {
        this(user.getId(), user.getUserName(), user.getUserEmail());
    }
}
