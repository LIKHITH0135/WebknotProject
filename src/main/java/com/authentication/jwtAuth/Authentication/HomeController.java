package com.authentication.jwtAuth.Authentication;

import com.authentication.jwtAuth.Service.UserService;
import com.authentication.jwtAuth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUser(){
        System.out.println("getting users");
        return this.userService.getUser();
    }
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsersAsAdmin() {
        List<User> users = userService.getAllUsersAsAdmin();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }

    @DeleteMapping("/admin/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
