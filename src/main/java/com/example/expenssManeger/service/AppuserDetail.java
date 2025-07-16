package com.example.expenssManeger.service;

import com.example.expenssManeger.Repository.ProfileRepo;
import com.example.expenssManeger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppuserDetail implements UserDetailsService
{

    private  final ProfileRepo profileRepo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

   ProfileEntity existing= profileRepo.findByEmail(email)
              .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return User.builder()
                .username(existing.getEmail())
                .password(existing.getPassword())
                .authorities(Collections.emptyList())
                .build();


    }
}
