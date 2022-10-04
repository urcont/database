package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.factory.UserBookTestFactory;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.KeyHolderFactory;
import com.edu.ulab.app.service.impl.UserServiceImplTemplate;
import com.edu.ulab.app.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTemplateTest {
    @InjectMocks
    UserServiceImplTemplate userService;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    KeyHolderFactory keyHolderFactory;

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
        KeyHolder keyHolder = keyHolderFactory.getKeyHolderWithId(1L);

        //when
        when(keyHolderFactory.getKeyHolder()).thenReturn(keyHolder);

        //then
        UserDto userDtoResult = userService.createUser(userDto);
        UserBookTestFactory.assertUserDto(userDtoResult, 1L, 11, "test name", "test title");
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {
        //given
        UserDto userDto = UserBookTestFactory.getUserDto(null, 11, "test name", "test title");
        UserDto result = UserBookTestFactory.getUserDto(101L, 11, "name after update", "title after update");
        KeyHolder keyHolder = keyHolderFactory.getKeyHolderWithId(1L);

        //when
        when(keyHolderFactory.getKeyHolder()).thenReturn(keyHolder);

        //then
        userDto = userService.createUser(userDto);
        UserBookTestFactory.assertUserDto(userDto, 1L, 11, "test name", "test title");
        userDto = userService.updateUser(result);
        UserBookTestFactory.assertUserDto(userDto, 101L, 11, "name after update", "title after update");
    }

    // * failed
    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
