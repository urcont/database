package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService {
    /**
     * Creates user in database
     * @param userDto - user (DTO) to create
     * @return updated userDto with id
     */
    UserDto createUser(UserDto userDto);

    /**
     * Updates user in database from userDto, user id must exists
     * @param userDto - user (DTO) to update
     * @return updated userDto
     */
    UserDto updateUser(UserDto userDto);

    /**
     * Gets information from repository and returns it as UserDto
     * @param id - user id
     * @return - UserDto
     */
    UserDto getUserById(Long id);

    /**
     * Deletes user with id
     * @param id - user id
     */
    void deleteUserById(Long id);
}
