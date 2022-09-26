package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;
    private final Validator validator;

    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate, Validator validator) {
        this.jdbcTemplate = jdbcTemplate;
        this.validator = validator;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, bookDto.getTitle());
                    ps.setString(2, bookDto.getAuthor());
                    ps.setLong(3, bookDto.getPageCount());
                    ps.setLong(4, bookDto.getUserId());
                    return ps;
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        final String INSERT_SQL = "UPDATE BOOK SET Title = ?, Author = ?, Page_Count = ?, User_Id = ?" +
                " WHERE id = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final Long id = bookDto.getId();
        validator.checkBookIdNotNull(Optional.ofNullable(id));
        int status = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{});
                    ps.setString(1, bookDto.getTitle());
                    ps.setString(2, bookDto.getAuthor());
                    ps.setLong(3, bookDto.getPageCount());
                    ps.setLong(4, bookDto.getUserId());
                    ps.setLong(5, id);
                    return ps;
                }, keyHolder);

        validator.checkBookProcessed(status, id);
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        final String SELECT_SQL = "SELECT ID, TITLE, AUTHOR, PAGE_COUNT, USER_ID FROM BOOK WHERE ID = ?";

        var res = jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SELECT_SQL,
                            new String[]{"ID"});
                    ps.setLong(1, id);
                    return ps;
                },
                (rs, rowNum) -> {
                    BookDto newBook = new BookDto();
                    newBook.setId(rs.getLong("ID"));
                    newBook.setTitle(rs.getString("TITLE"));
                    newBook.setAuthor(rs.getString("AUTHOR"));
                    newBook.setPageCount(rs.getLong("PAGE_COUNT"));
                    newBook.setUserId(rs.getLong("USER_ID"));
                    return newBook;
                }
        );
        return validator.checkBookExists(res, id);
    }

    @Override
    public void deleteBookById(Long id) {
        final String DELETE_SQL = "DELETE from BOOK where ID = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(DELETE_SQL, new String[]{});
                    ps.setLong(1, id);
                    return ps;
                }, keyHolder);
    }

    @Override
    public List<Long> getListBookByUserId(Long userId) {
        final String SELECT_SQL = "SELECT ID FROM BOOK WHERE USER_ID = ?";

        return jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SELECT_SQL,
                            new String[]{"ID"});
                    ps.setLong(1, userId);
                    return ps;
                },
                (rs, rowNum) -> rs.getLong("ID")
        );
    }

    @Override
    public void deleteBookByUserId(Long userId) {
        final String DELETE_SQL = "DELETE from BOOK where USER_ID = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(DELETE_SQL, new String[]{});
                    ps.setLong(1, userId);
                    return ps;
                }, keyHolder);
    }
}
