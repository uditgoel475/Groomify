package com.niit.lookatme.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.role.Role;
import com.niit.lookatme.dao.role.RoleName;

@Repository("roleRepository")
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

	Optional<Role> findByName(RoleName name);
}
