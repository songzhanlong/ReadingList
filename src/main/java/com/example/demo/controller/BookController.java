package com.example.demo.controller;

import com.example.demo.dao.BookRepository;
import com.example.demo.domain.AmazonProperties;
import com.example.demo.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by songzhanlong on 2018/4/13.
 */
@Controller
@RequestMapping("/")
public class BookController {
    private BookRepository bookRepository;
    AmazonProperties amazonProperties;

    @Autowired
    public void setAmazonProperties(AmazonProperties amazonProperties) {
        this.amazonProperties = amazonProperties;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RequestMapping(value = "/{reader}", method = RequestMethod.GET)
    public String readersBooks(
            @PathVariable("reader") String reader,
            Model model) {
        List<Book> readingList =
                bookRepository.findByReader(reader);
        if (readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
            model.addAttribute("amazonID", amazonProperties.getAssociateId());
        }
        return "bookList";
    }

    @RequestMapping(value = "/{reader}", method = RequestMethod.POST)
    public String addToReadingList(
            @PathVariable("reader") String reader, Book book) {
        book.setReader(reader);
        bookRepository.save(book);
        return "redirect:/{reader}";
    }
}
