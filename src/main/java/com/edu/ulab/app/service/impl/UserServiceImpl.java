package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           Validator validator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        Person savedUser = userRepository.save(user);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        Long userId = userDto.getId();
        validator.checkPersonNotNull(
                userRepository.findById(userId), userId
        );
        return userMapper.personToUserDto(
                userRepository.save(
                        userMapper.userDtoToPerson(userDto)
                )
        );
    }

    @Override
    public UserDto getUserById(Long id) {
        Person foundPerson = validator.checkPersonNotNull(
                userRepository.findById(id), id
        );

        return userMapper.personToUserDto(foundPerson);
    }

    @Override
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id))
            userRepository.deleteById(id);
    }
}
