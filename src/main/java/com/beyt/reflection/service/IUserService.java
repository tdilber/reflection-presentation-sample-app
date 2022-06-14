package com.beyt.reflection.service;

import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.exception.UserNotFoundException;

import java.util.List;

public interface IUserService {
    List<UserDTO> getUserList() throws UserNotFoundException;

    UserDTO getUserById(Long id) throws UserNotFoundException;
}
