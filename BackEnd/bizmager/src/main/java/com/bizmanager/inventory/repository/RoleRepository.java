package com.bizmanager.inventory.repository;

import com.bizmanager.inventory.model.TbRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<TbRoles,Long> {

    @Override
    Optional<TbRoles> findById(Long id);
}
