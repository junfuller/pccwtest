package org.example.pccwtest.repository;

import org.example.pccwtest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entity.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * and custom query methods for the {@link User} entity.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a {@link User} by their email address.
     *
     * @param email the email address of the user
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a {@link User} by their username.
     *
     * @param username the username of the user
     * @return an {@link Optional} containing the {@link User} if found, otherwise empty
     */
    Optional<User> findByUsername(String username);
}
