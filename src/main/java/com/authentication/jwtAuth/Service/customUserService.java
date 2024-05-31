package com.authentication.jwtAuth.Service;

import com.authentication.jwtAuth.entities.User;
import com.authentication.jwtAuth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class customUserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User Not found!!"));

        return user;
    }
}
