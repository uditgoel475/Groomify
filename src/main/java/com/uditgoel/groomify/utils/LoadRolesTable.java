package com.uditgoel.groomify.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.uditgoel.groomify.dao.repository.RoleRepository;
import com.uditgoel.groomify.dao.role.Role;
import com.uditgoel.groomify.dao.role.RoleName;

@Component
public class LoadRolesTable {

	private final RoleRepository roleRepository;

	public LoadRolesTable(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@PostConstruct
	@Transactional
	public void init() {

		if (roleRepository.count() == 0) {
			List<RoleName> roleNameList = new ArrayList<>(EnumSet.allOf(RoleName.class));

			roleRepository
					.saveAll(roleNameList.stream().map(Role::new).collect(Collectors.toList()));
		}
	}
}
