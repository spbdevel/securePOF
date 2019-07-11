package org.app.repository;


import org.app.entity.AllowedFields;
import org.app.entity.Dish;
import org.app.entity.Restaurant;
import org.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AllowedFieldsRepository extends JpaRepository<AllowedFields, Long> {


    AllowedFields findByRoleAndObjectName(Role role, String objectName);

}
