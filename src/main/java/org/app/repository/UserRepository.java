package org.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.app.entity.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountName(String name);
}
