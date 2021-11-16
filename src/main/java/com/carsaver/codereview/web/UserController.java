package com.carsaver.codereview.web;

import com.carsaver.codereview.model.User;
import com.carsaver.codereview.repository.UserRepository;
import com.carsaver.codereview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    //I would typically have the repository only visible by the service layer, and have the service available in the
    // controllers, but there is no real business logic in the service layer that is used (there is business logic,
    // but those methods are unused), so for consistency with the other controller, I am exposing the repository
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    private List<User> findAll(){
        return userRepository.findAllByOrderByIdAsc();
    }

    @GetMapping("/users/{id}")
    private ResponseEntity<User> findById(@PathVariable Long id){
        return ResponseEntity.of(userRepository.findById(id));
    }

}
