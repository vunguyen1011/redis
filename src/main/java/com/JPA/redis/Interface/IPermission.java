package com.JPA.redis.Interface;

import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public interface IPermission extends GrantedAuthority{
	
Integer getId();
   
    String getAuthority();
    void setAuthority(String authority);
	
}
