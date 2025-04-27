package com.JPA.redis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JPA.redis.Model.UserPermission;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission,Long> {
	
}
