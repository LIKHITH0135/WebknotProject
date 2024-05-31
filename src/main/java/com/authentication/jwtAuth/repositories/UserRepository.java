package com.authentication.jwtAuth.repositories;

import com.authentication.jwtAuth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {


    public Optional<User> findByEmail(String email);

    //void deleteByUserId(String userId);

    //boolean existsByUserId(String userId);

    boolean existsByEmail(String email);
}
