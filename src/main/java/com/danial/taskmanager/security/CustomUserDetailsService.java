package com.danial.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.danial.taskmanager.model.User;
import com.danial.taskmanager.repository.UserRepository;

/**
 * Custom UserDetailsService implementation
 * Loads user from database for spring security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Load user by username
     * Called by spring security during authentication
     * @param username - The username to search for
     * @return UserDetails object
     * @throws UsernameNotFoundException is user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //Find user in database
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: "+username));

        //Wrap our User entity in CustomUserDetails
        return new CustomUserDetails(user);
    }
}
