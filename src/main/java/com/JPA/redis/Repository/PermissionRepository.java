package com.JPA.redis.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.JPA.redis.Model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
	Optional<Permission> findByAuthority(String authority);
	boolean existsByAuthority (String authority);

}
