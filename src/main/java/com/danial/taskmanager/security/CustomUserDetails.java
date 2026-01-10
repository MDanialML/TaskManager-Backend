package com.danial.taskmanager.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.danial.taskmanager.model.User;

/**
 * Custom implementation of UserDetails
 * wraps our User entity for a Spring security
 */

public class CustomUserDetails implements UserDetails {

    private final User user;

    //constructor - wrap our User entity
    public CustomUserDetails(User user){
        this.user = user;
    }

    //Returns the username
    @Override
    public String getUsername(){
        return user.getUsername();
    }

    //returns the password
    @Override
    public String getPassword(){
        return user.getPassword();
    }

    /**
     * Returns the authorities(roles/permissions)
     * For now, we give everyone a User role
     */


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//simple implementation - all users have "ROLE_USER"
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	/**
     * Account is not expired
     * You can add expiration logic later if needed
     */
    @Override
    public boolean isAccountNonExpired(){
        return true; //our account don't expire
    }

    /**
     * Account is not locked
     * You can add locking logic later is needed
     */
    @Override
    public boolean isAccountNonLocked(){
        return true; //we don't lock account yet
    }


    /**
     * Credential are not expired
     * You can add password expiration later if needed
     */

    @Override
    public boolean isCredentialsNonExpired(){
        return true; //Password don't expire (yet)
    }

    /**
     * Account is enabled
     * Uses the 'enabled field from our User entity'
     */
    @Override
    public boolean isEnabled(){
        return user.getEnabled();
    }

    /**
     * Get the underlying User entity
     * Useful when we need access to id, email, etc
     */
    public User getUser(){
        return user;
    }
}
