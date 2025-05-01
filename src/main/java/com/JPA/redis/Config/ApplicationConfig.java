package com.JPA.redis.Config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.JPA.redis.Model.Permission;
import com.JPA.redis.Repository.PermissionRepository;


@Configuration
public class ApplicationConfig {

	@Bean
	ApplicationRunner applicationRunner(PermissionRepository permisionRepository) {
		return args->{
			if(!permisionRepository.existsByAuthority("USER")) {
			Permission permission =new Permission();
			permission.setAuthority("USER");
			permisionRepository.save(permission);
			}
		};
		
	}
}
