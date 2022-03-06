package com.beyt.reflection.controller;

import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.exception.UserNotFoundException;
import com.beyt.reflection.service.MetricsTestService;
import com.beyt.reflection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
