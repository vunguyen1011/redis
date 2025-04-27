package com.JPA.redis.Interface;

import org.springframework.security.core.userdetails.UserDetails;

public interface  IUser extends UserDetails {
	String getUsername();
	void setUsername(String username);
	String getPassword();
	void setPassword(String password);
}
