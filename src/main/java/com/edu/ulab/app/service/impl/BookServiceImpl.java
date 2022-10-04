package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final Validator validator;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, Validator validator) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.validator = validator;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Long bookId = bookDto.getId();
        validator.checkBookNotNull(
                bookRepository.findById(bookId), bookId
        );
        return bookMapper.bookToBookDto(
                bookRepository.save(
                        bookMapper.bookDtoToBook(bookDto)
                )
        );
    }

    @Override
    public BookDto getBookById(Long id) {
        Book foundBook = validator.checkBookNotNull(
                bookRepository.findById(id), id
        );
        return bookMapper.bookToBookDto(foundBook);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Long> getListBookByUserId(Long userId) {
        return validator.checkListBookIdNotNull(
                bookRepository.getBooksByUserId(userId)
        );
    }

    @Override
    public void deleteBookByUserId(Long userId) {
        bookRepository.deleteByPersonId(userId);
    }
}
