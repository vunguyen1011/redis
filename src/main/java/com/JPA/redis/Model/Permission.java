package com.JPA.redis.Model;

import java.util.List;
import java.util.UUID;

import com.JPA.redis.Interface.IPermission;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Permission implements IPermission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	private String authority;
	@OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<UserPermission> userPermissions;


}
