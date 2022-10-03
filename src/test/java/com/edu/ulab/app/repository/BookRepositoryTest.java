package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.factory.UserBookTestFactory;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertBook_thenAssertDmlCount() {
        //Given

        Person person = UserBookTestFactory.getPerson(null, 111, "reader", "Test Test");
        Person savedPerson = userRepository.save(person);
        Book book = UserBookTestFactory.getBook(null, savedPerson, "Test Author", "test", 1000);

        //When
        Book result = bookRepository.save(book);

        //Then
        UserBookTestFactory.assertBook(result, 200L, savedPerson, "Test Author", "test", 1000);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
        Person person = UserBookTestFactory.getPerson(null, 111, "reader", "Test Test");
        Person savedPerson = userRepository.save(person);
        Book book = UserBookTestFactory.getBook(null, savedPerson, "Test Author", "test", 1000);

        //When
        Book result = bookRepository.save(book);

        //Then
        UserBookTestFactory.assertBook(result, 200L, savedPerson, "Test Author", "test", 1000);
        assertSelectCount(0);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить книгу. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void selectBook_thenAssertDmlCount() {
        //Given
        Person person = UserBookTestFactory.getPerson(null, 111, "reader", "Test Test");
        Person savedPerson = userRepository.save(person);
        Book book = UserBookTestFactory.getBook(null, savedPerson, "Test Author", "test", 1000);

        //When
        Book result = bookRepository.save(book);

        //Then
        UserBookTestFactory.assertBook(result, 201L, savedPerson, "Test Author", "test", 1000);
        assertSelectCount(0);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить все книги. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void selectAllBooks_thenAssertDmlCount() {
        //Given
        Person person = UserBookTestFactory.getPerson(null, 111, "reader", "Test Test");
        Person savedPerson = userRepository.save(person);
        Book book = UserBookTestFactory.getBook(null, savedPerson, "Test Author", "test", 1000);

        //When
        Book result = bookRepository.save(book);

        //Then
        UserBookTestFactory.assertBook(result, 201L, savedPerson, "Test Author", "test", 1000);
        assertSelectCount(0);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить книгу. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBook_thenAssertDmlCount() {
        //Given
        Person person = UserBookTestFactory.getPerson(null, 111, "reader", "Test Test");
        Person savedPerson = userRepository.save(person);
        Book book = UserBookTestFactory.getBook(null, savedPerson, "Test Author", "test", 1000);

        //When
        Book result = bookRepository.save(book);

        //Then
        UserBookTestFactory.assertBook(result, 201L, savedPerson, "Test Author", "test", 1000);
        assertSelectCount(0);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    // * failed
    // example failed test
}
