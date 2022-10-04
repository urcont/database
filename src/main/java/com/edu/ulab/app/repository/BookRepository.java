package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteByPersonId(Long userId);

    @Query("select b.id from Book b where b.person.id = :userId")
    Optional <List<Long>> getBooksByUserId(Long userId);

}
