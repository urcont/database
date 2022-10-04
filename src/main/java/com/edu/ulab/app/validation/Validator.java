package com.edu.ulab.app.validation;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of validation layer
 */
@Component
public class Validator {
    public Person checkPersonNotNull(Optional<Person> res, Long id) {
        if (res.isEmpty())
            throw new PersonNotFoundException(id);
        return res.get();
    }

    public UserDto checkPersonExists(List<UserDto> list, Long id) {
        if (list.isEmpty())
            throw new PersonNotFoundException(id);
        return list.get(0);
    }

    public void checkPersonProcessed(int status, Long id) {
        if(status == 0)
            throw new PersonNotFoundException(id);
    }

    public Book checkBookNotNull(Optional<Book> res, Long id) {
        if (res.isEmpty())
            throw new BookNotFoundException(id);
        return res.get();
    }

    public BookDto checkBookExists(List<BookDto> list, Long id) {
        if (list.isEmpty())
            throw new BookNotFoundException(id);
        return list.get(0);
    }

    public void checkBookProcessed(int status, Long id) {
        if(status == 0)
            throw new BookNotFoundException(id);
    }

    public List<Long> checkListBookIdNotNull(Optional<List<Long>> res) {
        if (res.isEmpty())
            return new ArrayList<>();
        return res.get();
    }

}
