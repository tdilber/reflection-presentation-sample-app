package com.beyt.reflection.service;

import com.beyt.reflection.dto.UserDTO;
import com.beyt.reflection.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    public List<UserDTO> userList;

    public UserService() {
        userList = new ArrayList<>();
        userList.add(new UserDTO(1L, "Zeynep", "0555555555"));
        userList.add(new UserDTO(2L, "Mahmut", "0566554444"));
        userList.add(new UserDTO(3L, "Ayşe", "0588994433"));
    }


    public UserDTO getUserById(Long id) throws UserNotFoundException {
        Map<Long, UserDTO> userMap = getUserMap();

        if (userMap.containsKey(id)) {
            return userMap.get(id);
        }

        throw new UserNotFoundException();
    }

    private Map<Long, UserDTO> getUserMap() {
        return userList.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));
    }
}
