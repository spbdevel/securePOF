package org.app.repository;


import org.app.entity.UserField;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserFieldRepository extends JpaRepository<UserField, Long> {
}
