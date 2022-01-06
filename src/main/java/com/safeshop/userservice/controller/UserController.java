package com.safeshop.userservice.controller;

import com.safeshop.userservice.model.User;
import com.safeshop.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // Method to generate a random alphanumeric password of a specific length
    public static String generateRandomPassword(int len)
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    @PostConstruct
    public void fillDb(){
        userRepository.deleteAll();
        if (userRepository.count()==0){
            userRepository.save(new User("1","navid", "navid1", "navid7373", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE));
            userRepository.save(new User("Saman", "Saman", "navid7373", Boolean.TRUE,Boolean.FALSE,Boolean.TRUE));
            userRepository.save(new User("Ward", "Ward", "navid7373", Boolean.TRUE,Boolean.FALSE,Boolean.FALSE));

        }
    }

    @PostMapping("/users/login")
    public User loginUser(@RequestBody User user){
        User retUser = userRepository.findUserByUsername(user.getUsername());
        User emptyUser = new User();
        if (user.getPasswordHash().equals(user.getPasswordHash())){
            return retUser;
        }else{
            return emptyUser;
        }
    }

    @PostMapping("/users/newuser")
    public User newUser(@RequestBody User user){
        User emptyUser = new User();
        String Password = generateRandomPassword(8);

        if (userRepository.findUserByUsername(user.getUsername()) != null){
            return emptyUser;
        }else{
            user.setPasswordHash(Password);
            userRepository.save(user);
            return user;
        }
    }

    @GetMapping("/users/user/{username}")
    public User getUserById(@PathVariable String username){
        return userRepository.findUserByUsername(username);
    }

    @PutMapping("/users/user/{id}")
    public User putUserById(@PathVariable String id, @RequestBody User updatedUser){
        User retrievedUser = userRepository.findById(id).get();

        retrievedUser.setActive(updatedUser.getActive());
        retrievedUser.setAdmin(updatedUser.getAdmin());
        retrievedUser.setSeller(updatedUser.getSeller());
        retrievedUser.setName(updatedUser.getName());
        retrievedUser.setModifiedAt(LocalDateTime.now());

        userRepository.save(retrievedUser);
        return retrievedUser;
    }

    @DeleteMapping("/users/user/{id}")
    public ResponseEntity deleteUser(@PathVariable String id){
        User user = userRepository.findById(id).get();
        if (user != null){
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }


}
