package com.example.demo.dao;

import com.example.demo.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by songzhanlong on 2018/4/13.
 */
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findByReader(String reader);
}
