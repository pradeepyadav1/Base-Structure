package com.tech.webservice;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionEvaluator {

	public <T> boolean hasAccess(UserDetails user, String permissionPrefix, AbstractRestService< T> restService) {
		
		System.out.println("Permission Prefix : "+permissionPrefix);
		System.out.println("Service class : "+restService);
		final String permissionSuffix = "";//restService.getPermissionNameSuffix();
		System.out.println("Permission Suffix : "+permissionSuffix);
		
		final String permission = permissionPrefix + permissionSuffix;
		
        for (GrantedAuthority auth : user.getAuthorities()) {
            if (auth.getAuthority().startsWith(permission) )
                return (true);
        }
        return (false);
    }
	
}
