package dev.timjelenz.openlocationapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.timjelenz.openlocationapi.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByUserEmail(String userEmail);
}
