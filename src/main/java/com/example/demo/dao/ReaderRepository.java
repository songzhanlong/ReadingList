package com.example.demo.dao;

import com.example.demo.domain.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository
        extends JpaRepository<Reader, String> {
}
