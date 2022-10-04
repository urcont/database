package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.factory.UserBookTestFactory;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = UserBookTestFactory.getPerson(null, 111, "reader", "Test Test");

        //When
        Person result = userRepository.save(person);

        //Then
        UserBookTestFactory.assertUser(result, 100L, 111, "reader", "Test Test");
        UserBookTestFactory.assertDmlCount(1, 0, 0, 0);
    }

    @DisplayName("Обновить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
        //Given
        Long userId = 1L;

        //When
        Person result = userRepository.findById(userId).orElse(new Person());
        result.setAge(99);
        result = userRepository.save(result);

        //Then
        UserBookTestFactory.assertUser(result, 1L, 99, "user for book", "reader for book");
        UserBookTestFactory.assertDmlCount(1, 0, 0, 0);
    }

    @DisplayName("Получить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void selectPerson_thenAssertDmlCount() {
        //Given
        Long userId = 1L;

        //When
        Person result = userRepository.findById(userId).orElse(new Person());

        //Then
        UserBookTestFactory.assertUser(result, 1L, 33, "user for book", "reader for book");
        UserBookTestFactory.assertDmlCount(1, 0, 0, 0);
    }

    @DisplayName("Удалить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePerson_thenAssertDmlCount() {
        //Given
        Long userId = 1L;

        //When
        userRepository.deleteById(userId);

        //Then
        UserBookTestFactory.assertDmlCount(1, 0, 0, 0);
    }

    // * failed
}
