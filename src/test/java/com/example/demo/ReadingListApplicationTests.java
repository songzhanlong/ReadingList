package com.example.demo;

import com.example.demo.dao.BookRepository;
import com.example.demo.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReadingListApplicationTests {
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void contextLoads() {
        Book book = new Book();
        book.setReader("tom");
        book.setAuthor("jerry");
        book.setTitle("helloworld");
        bookRepository.save(book);
        List<Book> bookList = bookRepository.findByReader("tom");
        Book book1 = bookList.get(0);
        assertEquals("jerry", book1.getAuthor());
        assertEquals("helloworld", book1.getTitle());
    }

}
