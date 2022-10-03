package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {

    private final JdbcTemplate jdbcTemplate;
    private final Validator validator;

    public UserServiceImplTemplate(JdbcTemplate jdbcTemplate, Validator validator) {
        this.jdbcTemplate = jdbcTemplate;
        this.validator = validator;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        final String INSERT_SQL = "INSERT INTO ulab_edu.person(FULL_NAME, TITLE, AGE, ID) " +
                "VALUES (?,?,?, nextval('sequenceUser'))";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        final String INSERT_SQL = "UPDATE ulab_edu.person SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE id = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final Long id = userDto.getId();
        int status = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    ps.setLong(4, id);
                    return ps;
                }, keyHolder);
        validator.checkPersonProcessed(status, id);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        final String SELECT_SQL = "SELECT ID, AGE, TITLE, FULL_NAME FROM ulab_edu.person WHERE ID = ?";
        var res = jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SELECT_SQL,
                            new String[]{"ID", "AGE", "TITLE", "FULL_NAME"});
                    ps.setLong(1, id);
                    return ps;
                },
                (rs, rowNum) -> {
                    UserDto newPerson = new UserDto();
                    newPerson.setId(rs.getLong("ID"));
                    newPerson.setFullName(rs.getString("FULL_NAME"));
                    newPerson.setTitle(rs.getString("TITLE"));
                    newPerson.setAge(rs.getInt("AGE"));
                    return newPerson;
                }
        );
        return validator.checkPersonExists(res, id);
    }

    @Override
    public void deleteUserById(Long id) {
        final String DELETE_SQL = "DELETE from ulab_edu.person where ID = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int status = jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(DELETE_SQL, new String[]{});
                    ps.setLong(1, id);
                    return ps;
                }, keyHolder);
    }
}
