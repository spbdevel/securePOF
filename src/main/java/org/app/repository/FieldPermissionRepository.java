package org.app.repository;


import org.app.entity.FieldPermission;
import org.app.entity.Role;
import org.app.entity.UserField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FieldPermissionRepository extends JpaRepository<FieldPermission, Long> {

    @Query("SELECT fd FROM FieldPermission fd WHERE fd.field = :field and fd.role = :role")
    public Optional<FieldPermission> findByFieldAndRole(@Param("field") UserField field, @Param("role") Role role);
}
