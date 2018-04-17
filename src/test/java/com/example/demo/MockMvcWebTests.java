package com.example.demo;

import com.example.demo.domain.Book;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockMvcWebTests {
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void homePage() throws Exception {
        mockMvc.perform(get("/tom")).andExpect(status().isOk()).andExpect(view().name("bookList")).andExpect(model().attributeExists("books")).andExpect(model().attribute("books",
                Matchers.is(Matchers.empty())));
    }

    @Test
    public void postBook() throws Exception {
        mockMvc.perform(post("/tom")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "BOOK TITLE")
                .param("author", "BOOK AUTHOR")
                .param("isbn", "1234567890").param("description", "DESCRIPTION"))
                .andExpect(status().is3xxRedirection()).andExpect(header().string("Location", "/tom"));
        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setReader("tom");
        expectedBook.setTitle("BOOK TITLE");
        expectedBook.setAuthor("BOOK AUTHOR");
        expectedBook.setIsbn("1234567890");
        expectedBook.setDescription("DESCRIPTION");
        mockMvc.perform(get("/tom"))
                .andExpect(status().isOk()).andExpect(view().name("bookList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)))
                .andExpect(model().attribute("books", contains(samePropertyValuesAs(expectedBook))));

    }
}
