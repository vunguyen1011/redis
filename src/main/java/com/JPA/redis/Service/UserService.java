package com.JPA.redis.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.JPA.redis.DTO.Request.CreateUserRequest;
import com.JPA.redis.Model.Permission;
import com.JPA.redis.Model.User;
import com.JPA.redis.Model.UserPermission;
import com.JPA.redis.Repository.PermissionRepository;
import com.JPA.redis.Repository.UserPermissionRepository;
import com.JPA.redis.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final UserPermissionRepository userPermissionRepository;
	private final PermissionRepository permissionRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		
		return userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
		
	}
	public User createUser(CreateUserRequest request) {
		if(userRepository.existsByUsername(request.getUsername())) throw new RuntimeException("username đã tồn tại");
		Permission permission =permissionRepository.findByAuthority("USER").orElseThrow(()->new RuntimeException("permission not found"));
		String password=passwordEncoder.encode(request.getPassword());
		User user=User.builder()
				.username(request.getUsername())
				.password(password)
				 .userPermissions(new ArrayList<>()) // quan trọng!
				.build();
		userRepository.save(user);
		UserPermission userPermission=UserPermission.builder()
				
				.user(user)
				.permission(permission)
				.build();
		userPermissionRepository.save(userPermission);
		user.getUserPermissions().add(userPermission);
		userRepository.save(user);
		
		
		
		
		return user;
	}
	
}
