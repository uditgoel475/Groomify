package com.uditgoel.groomify.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.role.Role;
import com.uditgoel.groomify.dao.role.RoleName;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
}
