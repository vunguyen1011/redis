package com.JPA.redis.Service;

import java.util.ArrayList;
import java.util.List;

import com.JPA.redis.DTO.Response.UserResponse;
import com.JPA.redis.Exception.ErrorCode;
import com.JPA.redis.Exception.WebException;
import com.JPA.redis.Mapper.UserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final UserPermissionRepository userPermissionRepository;
	private final PermissionRepository permissionRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new WebException(ErrorCode.USER_NOT_FOUND));
	}

	// ✅ Tạo user và xoá cache danh sách user
	@CacheEvict(value = "userList", allEntries = true)
	public UserResponse createUser(CreateUserRequest request) {
		if (userRepository.existsByUsername(request.getUsername()))
			throw new WebException(ErrorCode.USERNAME_ALREADY_EXISTS);

		Permission permission = permissionRepository.findByAuthority("USER")
				.orElseThrow(() -> new WebException(ErrorCode.USERNAME_ALREADY_EXISTS));

		String password = passwordEncoder.encode(request.getPassword());
		User user = User.builder()
				.username(request.getUsername())
				.password(password)
				.userPermissions(new ArrayList<>())
				.build();

		userRepository.save(user);

		UserPermission userPermission = UserPermission.builder()
				.user(user)
				.permission(permission)
				.build();

		userPermissionRepository.save(userPermission);
		user.getUserPermissions().add(userPermission);
		userRepository.save(user);

		return userMapper.toResponse(user);
	}

	// ✅ Cache danh sách user (key = "userList")
	@Cacheable(value = "userList")
	public List<UserResponse> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(userMapper::toResponse).toList();
	}

	// ✅ Cache từng user theo key "users::username"
	@Cacheable(value = "users", key = "#username")
	public UserResponse getUserResponseByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new WebException(ErrorCode.USER_NOT_FOUND));
		return userMapper.toResponse(user);
	}

	// ✅ Update user → cập nhật cache riêng & xoá danh sách
	@CachePut(value = "users", key = "#username")
	@CacheEvict(value = "userList", allEntries = true)
	public UserResponse updateUserResponseByUsername(String username, String newPassword) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new WebException(ErrorCode.USER_NOT_FOUND));

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		return userMapper.toResponse(user);
	}

	// ✅ Xoá cache riêng & cache danh sách khi xoá user
	@CacheEvict(value = { "users", "userList" }, key = "#username", allEntries = false)
	public void deleteUserResponseByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new WebException(ErrorCode.USER_NOT_FOUND));

		userPermissionRepository.deleteAll(user.getUserPermissions());
		userPermissionRepository.flush();
		userRepository.delete(user);
		userRepository.flush();
	}
}
