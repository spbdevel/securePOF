package org.app.repository;


import org.app.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDataRepository extends JpaRepository<UserData, Long> {
}
