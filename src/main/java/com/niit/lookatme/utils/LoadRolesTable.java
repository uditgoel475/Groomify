package com.niit.lookatme.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.niit.lookatme.dao.repository.RoleRepository;
import com.niit.lookatme.dao.role.Role;
import com.niit.lookatme.dao.role.RoleName;

@Component
public class LoadRolesTable {

	@Resource
	private RoleRepository roleRepository;

	@PostConstruct
	public void init() {

		if (roleRepository.count() == 0) {
			List<RoleName> roleNameList = new ArrayList<>(EnumSet.allOf(RoleName.class));

			roleRepository
					.saveAll(roleNameList.stream().map(Role::new).collect(Collectors.toList()));
		}
	}
}
