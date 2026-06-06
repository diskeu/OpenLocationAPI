package dev.timjelenz.openlocationapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timjelenz.openlocationapi.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Find User with the given name.
     *
     * @param userName name of the user
     * @return a optional containing a user if one was found
     */
    Optional<User> findByUserName(String userName);

    /**
     * Find User with the given email.
     *
     * @param userEmail email of the user
     * @return a optional containing a user if one was found
     */
    Optional<User> findByUserEmail(String userEmail);
}
