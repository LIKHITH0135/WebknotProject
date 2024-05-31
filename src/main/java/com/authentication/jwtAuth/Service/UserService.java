package com.authentication.jwtAuth.Service;

import com.authentication.jwtAuth.entities.User;
import com.authentication.jwtAuth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {


//    private List<User> store =new ArrayList<>();
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> getUser() {

        return userRepository.findAll();
    }

    public User CreateUser(User user){

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        user.setUserID(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());
         return userRepository.save(user);
    }


    public List<User> getAllUsersAsAdmin() {
        return userRepository.findAll();
    }

    public void deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
