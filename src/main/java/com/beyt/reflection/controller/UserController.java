package com.beyt.reflection.controller;

import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.exception.UserNotFoundException;
import com.beyt.reflection.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
