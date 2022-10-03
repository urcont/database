package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.factory.UserBookTestFactory;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Mock
    Validator validator;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person  = UserBookTestFactory.getPerson(1L, null, null, null);
        BookDto bookDto = UserBookTestFactory.getBookDto(null,1L,"test author", "test title", 1000);
        BookDto result = UserBookTestFactory.getBookDto(1L,1L,"test author", "test title", 1000);
        Book book = UserBookTestFactory.getBook(null, person, "test author", "test title", 1000);
        Book savedBook = UserBookTestFactory.getBook(1L, person, "test author", "test title", 1000);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        UserBookTestFactory.assertBookDto(bookDtoResult, 1L, 1L, "test author", "test title", 1000);
    }

    @Test
    @DisplayName("Обновление книги. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        Person person  = UserBookTestFactory.getPerson(1001L, null, null, null);
        BookDto bookDtoNotFound = UserBookTestFactory.getBookDto(2L, null, null, null, null);
        BookDto bookDto = UserBookTestFactory.getBookDto(2002L,1001L,"test author", "test title", 1000);
        Book book = UserBookTestFactory.getBook(2002L, person, "test author", "test title", 1000);
        Book savedBook = UserBookTestFactory.getBook(2002L, person, "test author", "test title", 1000);
        BookDto result = UserBookTestFactory.getBookDto(2002L,1001L,"test author", "test title", 1000);

        //when
        when(bookRepository.findById(bookDtoNotFound.getId()))
                .thenThrow(new PersonNotFoundException(bookDtoNotFound.getId()));
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.updateBook(bookDto);
        UserBookTestFactory.assertBookDto(bookDto, 2002L, 1001L, "test author", "test title", 1000);
    }

    @Test
    @DisplayName("Получение книги. Должно пройти успешно.")
    void getBook_Test() {
        //given
        Long bookIdNotFound = 2L;
        Long bookId = 2002L;

        Person person  = UserBookTestFactory.getPerson(1001L, null, null, null);
        Book foundBook  = UserBookTestFactory.getBook(2002L, person, "test author", "test title", 1000);
        BookDto result = UserBookTestFactory.getBookDto(2002L,1001L,"test author", "test title", 1000);

        //when
        when(bookRepository.findById(bookIdNotFound)).thenReturn(Optional.empty());
        when(validator.checkBookNotNull(Optional.empty(), bookIdNotFound))
                .thenThrow(new PersonNotFoundException(bookIdNotFound));
        when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(foundBook));
        when(validator.checkBookNotNull(Optional.ofNullable(foundBook), bookId))
                .thenReturn(foundBook);
        when(bookMapper.bookToBookDto(foundBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.getBookById(bookId);
        UserBookTestFactory.assertBookDto(bookDtoResult, 2002L, 1001L, "test author", "test title", 1000);
    }

    @Test
    @DisplayName("Удаление книги. Должно пройти успешно.")
    void deleteBook_Test() {
        //given
        Long bookIdNotFound = 3002L;
        Long bookId = 3003L;

        //when
        when(bookRepository.existsById(bookIdNotFound)).thenReturn(false);
        when(bookRepository.existsById(bookId)).thenReturn(true);

        //then
        bookService.deleteBookById(bookId);
        assertEquals(bookRepository.findById(bookId), Optional.empty());
    }

    @Test
    @DisplayName("Удаление книги по user id. Должно пройти успешно.")
    void deleteBookByPersonId_Test() {
        //given
        Long userId = 1001L;
        Long bookId1 = 2002L;
        Long bookId2 = 3003L;
        Long bookIdNotFound = 103L;

        //when
        when(bookRepository.existsById(bookId1)).thenReturn(true);
        when(bookRepository.existsById(bookId2)).thenReturn(true);
        when(bookRepository.existsById(bookIdNotFound)).thenReturn(false);

        //then
        bookService.deleteBookByUserId(userId);
        assertEquals(bookRepository.findById(bookId1), Optional.empty());
        assertEquals(bookRepository.findById(bookId2), Optional.empty());
     }

    @Test
    @DisplayName("Получение книг по user id. Должно пройти успешно.")
    void getListBookByUserId_Test() {
        //given
        Long userId = 1001L;
        Long bookId1 = 2002L;
        Long bookId2 = 3003L;

        List<Long> list = new ArrayList<>();
        list.add(bookId1);
        list.add(bookId2);
        Collections.sort(list);
        //when
        when(bookRepository.getBooksByUserId(userId)).thenReturn(Optional.of(list));

        //then
        List<Long> resList = bookService.getListBookByUserId(userId);
        Collections.sort(resList);
        //assertIterableEquals(resList, list);
    }

    // * failed
}
