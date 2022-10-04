package com.edu.ulab.app.factory;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertDeleteCount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserBookTestFactory {
    public static UserDto getUserDto(Long id, Integer age, String fullName, String title) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setAge(age);
        userDto.setFullName(fullName);
        userDto.setTitle(title);
        return userDto;
    }

    public static BookDto getBookDto(Long id, Long userId, String author, String title, Integer pageCount) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setUserId(userId);
        bookDto.setAuthor(author);
        bookDto.setTitle(title);
        bookDto.setPageCount(pageCount);
        return bookDto;
    }

    public static Person getPerson(Long id, Integer age, String fullName, String title) {
        Person person = new Person();
        person.setId(id);
        person.setAge(age);
        person.setFullName(fullName);
        person.setTitle(title);
        return person;
    }

    public static Book getBook(Long id, Person person, String author, String title, Integer pageCount) {
        Book book = new Book();
        book.setId(id);
        book.setPerson(person);
        book.setAuthor(author);
        book.setTitle(title);
        book.setPageCount(pageCount);
        return book;
    }
    public static void assertUserDto(UserDto userDto, Long id, Integer age, String fullName, String title) {
        assertEquals(id, userDto.getId());
        assertEquals(age, userDto.getAge());
        assertEquals(fullName, userDto.getFullName());
        assertEquals(title, userDto.getTitle());
    }

    public static void assertBookDto(BookDto bookDto, Long id, Long userId, String author, String title, Integer pageCount) {
        assertEquals(id, bookDto.getId());
        assertEquals(userId, bookDto.getUserId());
        assertEquals(author, bookDto.getAuthor());
        assertEquals(title, bookDto.getTitle());
        assertEquals(pageCount, bookDto.getPageCount());
    }

    public static void assertUser(Person person, Long id, Integer age, String fullName, String title) {
        assertThat(person.getAge()).isEqualTo(age);
        assertThat(person.getFullName()).isEqualTo(fullName);
        assertThat(person.getTitle()).isEqualTo(title);
    }

    public static void assertBook(Book book, Long id, Person person, String author, String title, Integer pageCount) {
        assertThat(book.getPerson()).isEqualTo(person);
        assertThat(book.getAuthor()).isEqualTo(author);
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getPageCount()).isEqualTo(pageCount);
    }

    public static void assertDmlCount(long selectCount, long insertCount, long updateCount, long deleteCount) {
        assertSelectCount(selectCount);
        assertInsertCount(insertCount);
        assertUpdateCount(updateCount);
        assertDeleteCount(deleteCount);
    }
}
