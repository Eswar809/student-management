package com.eswardev.studentmanagement.dao;

import com.eswardev.studentmanagement.entity.Role;

public interface RoleDao {
	
	public Role findRoleByName(String theRoleName);
}
