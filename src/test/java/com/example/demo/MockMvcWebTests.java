package com.example.demo;

import com.example.demo.dao.ReaderRepository;
import com.example.demo.domain.Book;
import com.example.demo.domain.Reader;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockMvcWebTests {
    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;
    @Autowired
    private ReaderRepository readerRepository;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply(springSecurity()).build();
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

    @Test
    @WithMockUser(username = "craig",
            password = "password",
            roles = "READER")
    public void homePage_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("http://localhost:8080"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location",
                        "http://localhost:8080/login"));
    }

    @Test
    @WithUserDetails("craig")
    public void homePage_authenticatedUser() throws Exception {
        Reader expectedReader = new Reader();
        expectedReader.setUsername("craig");
        expectedReader.setPassword("password");
        expectedReader.setFullname("Craig Walls");
        readerRepository.save(expectedReader);
        mockMvc.perform(get("/craig"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attribute("reader",
                        samePropertyValuesAs(expectedReader))).andExpect(model().attribute("books", hasSize(0)));
    }
}
