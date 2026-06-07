package dev.timjelenz.openlocationapi.dto.responses.user;

import dev.timjelenz.openlocationapi.models.User;

public record PublicUserResponse(
    int id,
    String userName
) {
    public PublicUserResponse(User user) {
        this(user.getId(), user.getUserName());
    }
}
