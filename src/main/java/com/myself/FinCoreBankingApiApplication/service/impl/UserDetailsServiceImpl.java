package com.myself.FinCoreBankingApiApplication.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myself.FinCoreBankingApiApplication.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

            System.out.println("Email received: " + email);
            
            UserDetails user = userRepository.findByEmail(email);
            boolean user1 = userRepository.existsByEmail(email);
            
            System.out.println("User present: " + user +" "+user1);
            
            if (user==null) {
                throw new UsernameNotFoundException("No user found with email: " + email);
            }

            System.out.println("User: " + user);

            return user;
        }
}