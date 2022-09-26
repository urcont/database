package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;

public interface BookService {
    /**
     * Creates book in database
     * @param bookDto - book (DTO) to create
     * @return updated bookDto with id
     */
    BookDto createBook(BookDto bookDto);

    /**
     * Updates book in database from bookDto, book id must exists
     * @param bookDto - book (DTO) to update
     * @return updated bookDto
     */
    BookDto updateBook(BookDto bookDto);

    /**
     * Gets information from repository and returns it as BookDto
     * @param id - book id
     * @return - BookDto
     */
    BookDto getBookById(Long id);

    /**
     * Deletes book with id
     * @param id - book id
     */
    void deleteBookById(Long id);

    /**
     * Gets booklist of user
     * @param userId - user id
     * @return list of books for user
     */
    List<Long> getListBookByUserId(Long userId);

    /**
     * Deletes all books of user
     * @param userId - user id
     */
    void deleteBookByUserId(Long userId);
}
