package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.factory.UserBookTestFactory;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import com.edu.ulab.app.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    Validator validator;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given
        UserDto userDto = UserBookTestFactory.getUserDto(null, 11, "test name", "test title");
        Person person  = UserBookTestFactory.getPerson(null, 11, "test name", "test title");
        Person savedPerson  = UserBookTestFactory.getPerson(1L, 11, "test name", "test title");
        UserDto result = UserBookTestFactory.getUserDto(1L, 11, "test name", "test title");

        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then
        UserDto userDtoResult = userService.createUser(userDto);
        UserBookTestFactory.assertUserDto(userDtoResult, 1L, 11, "test name", "test title");
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {
        //given
        UserDto userDtoNotFound = UserBookTestFactory.getUserDto(102L, 11, "name after update", "title after update");
        UserDto userDto = UserBookTestFactory.getUserDto(101L, 11, "name after update", "title after update");
        Person person  = UserBookTestFactory.getPerson(101L, 11, "name after update", "title after update");
        Person savedPerson  = UserBookTestFactory.getPerson(101L, 11, "name after update", "title after update");
        UserDto result = UserBookTestFactory.getUserDto(101L, 11, "name after update", "title after update");

        //when
        when(userRepository.findById(userDtoNotFound.getId()))
                .thenThrow(new PersonNotFoundException(userDtoNotFound.getId()));
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then
        UserDto userDtoResult = userService.updateUser(userDto);
        UserBookTestFactory.assertUserDto(userDtoResult, 101L, 11, "name after update", "title after update");
    }

    @Test
    @DisplayName("Получение пользователя. Должно пройти успешно.")
    void getPerson_Test() {
        //given
        Long userIdNotFound = 103L;
        Long userId = 101L;
        Person foundPerson  = UserBookTestFactory.getPerson(101L, 55,"user for update", "reader for update");
        UserDto result = UserBookTestFactory.getUserDto(101L, 55,"user for update", "reader for update");

        //when
        when(userRepository.findById(userIdNotFound)).thenReturn(Optional.empty());
        when(validator.checkPersonNotNull(Optional.empty(), userIdNotFound))
                .thenThrow(new PersonNotFoundException(userIdNotFound));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(foundPerson));
        when(validator.checkPersonNotNull(Optional.ofNullable(foundPerson), userId))
                .thenReturn(foundPerson);
        when(userMapper.personToUserDto(foundPerson)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.getUserById(userId);
        UserBookTestFactory.assertUserDto(userDtoResult, 101L, 55,"user for update", "reader for update");
    }

    @Test
    @DisplayName("Удаление пользователя. Должно пройти успешно.")
    void deletePerson_Test() {
        //given
        Long userIdNotFound = 103L;
        Long userId = 101L;

        //when
        when(userRepository.existsById(userIdNotFound)).thenReturn(false);
        when(userRepository.existsById(userId)).thenReturn(true);

        //then
        userService.deleteUserById(userId);
        assertEquals(userRepository.findById(userId), Optional.empty());
    }

    // * failed
    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
