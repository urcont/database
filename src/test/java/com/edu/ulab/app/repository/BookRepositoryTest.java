package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.factory.UserBookTestFactory;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import net.ttddyy.dsproxy.QueryCountHolder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertDeleteCount;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
@Rollback
@Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
})
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу. Число select должно равняться 2, insert 1")
    @Test
    void insertBook_thenAssertDmlCount() {
        //Given
        Long userId = 101L;
        Person savedPerson = userRepository.findById(userId).orElse(new Person());
        Book book = UserBookTestFactory.getBook(null, savedPerson, "Test Author", "test", 1000);

        //When
        Book result = bookRepository.save(book);

        //Then
        userRepository.flush();
        bookRepository.flush();
        UserBookTestFactory.assertBook(result, 100L, savedPerson, "Test Author", "test", 1000);
        UserBookTestFactory.assertDmlCount(2, 1, 0, 0);
    }

    @DisplayName("Обновить книгу. Число select должно равняться 2, update 1")
    @Test
    void updateBook_thenAssertDmlCount() {
        //Given
        Long userId = 101L;
        Long bookId = 301L;

        //When
        Person resultUser = userRepository.findById(userId).orElse(new Person());
        Book resultBook = bookRepository.findById(bookId).orElse(new Book());
        resultBook.setTitle("new title");
        resultBook = bookRepository.save(resultBook);

        //Then
        userRepository.flush();
        bookRepository.flush();
        UserBookTestFactory.assertUser(resultUser, 101L, 55, "user for update", "reader for update");
        UserBookTestFactory.assertBook(resultBook, 301L, resultUser, "on more author2", "new title", 665);
        UserBookTestFactory.assertDmlCount(2, 0, 1, 0);
    }

    @DisplayName("Получить книгу. Число select должно равняться 2")
    @Test
    void selectBook_thenAssertDmlCount() {
        //Given
        Long userId = 101L;
        Long bookId = 301L;

        //When
        Person resultUser = userRepository.findById(userId).orElse(new Person());
        Book resultBook = bookRepository.findById(bookId).orElse(new Book());

        //Then
        userRepository.flush();
        bookRepository.flush();
        UserBookTestFactory.assertUser(resultUser, 101L, 55, "user for update", "reader for update");
        UserBookTestFactory.assertBook(resultBook, 301L, resultUser, "on more author2", "more default2 book", 665);
        UserBookTestFactory.assertDmlCount(2, 0, 0, 0);
    }

    @DisplayName("Получить все книги. Число select должно равняться 1")
    @Test
    void selectAllUserBooks_thenAssertDmlCount() {
        //Given
        Long userId = 1001L;

        //When
        List<Long> result = bookRepository.getBooksByUserId(userId).orElse(new ArrayList<>());

        //Then
        bookRepository.flush();
        UserBookTestFactory.assertDmlCount(1, 0, 0, 0);
    }

    @DisplayName("Удалить книгу. Число select должно равняться 1, delete 1")
    @Test
    void deleteBook_thenAssertDmlCount() {
        //Given
        Long bookId = 301L;

        //When
        bookRepository.deleteById(bookId);

        //Then
        bookRepository.flush();
        UserBookTestFactory.assertDmlCount(1, 0, 0, 1);
    }

    @DisplayName("Удалить все книги по user id. Число select должно равняться 1, delete 2")
    @Test
    void deleteBooksByUserId_thenAssertDmlCount() {
        //Given
        Long userId = 1001L;

        //When
        bookRepository.deleteByPersonId(userId);

        //Then
        bookRepository.flush();
        UserBookTestFactory.assertDmlCount(1, 0, 0, 2);
    }

    // * failed
    // example failed test
}
