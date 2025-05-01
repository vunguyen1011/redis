package com.JPA.redis.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.JPA.redis.Interface.IPermission;
import com.JPA.redis.Interface.IUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements IUser, Serializable {
	@Id
	
	private String username;
	private String password;
	@JsonIgnore
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	    private List<UserPermission> userPermissions=new ArrayList<>();
	 @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return userPermissions.stream()
	                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission().getAuthority()))
	                .collect(Collectors.toList());
	    }
	
	
}

