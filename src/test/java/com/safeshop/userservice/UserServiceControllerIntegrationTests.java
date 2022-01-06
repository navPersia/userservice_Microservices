package com.safeshop.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeshop.userservice.model.User;
import com.safeshop.userservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user1= new User("1","navid", "navid1", "123", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);
    private User user2= new User("2","Sa", "sa", "123", Boolean.TRUE,Boolean.TRUE,Boolean.TRUE);
    private User user3= new User("3","dirk", "dirk", "1234", Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);
    private User user4= new User("4","david", "david", "123", Boolean.TRUE,Boolean.FALSE,Boolean.TRUE);
    private User user5= new User("5","charlotte", "charlotte", "12345", Boolean.TRUE,Boolean.FALSE,Boolean.FALSE);

    @BeforeEach
    public void beforeAllTests() {
        userRepository.deleteAll();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        userRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testuserbyusername() throws Exception {

        mockMvc.perform(get("/users/user/{username}", "navid1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("navid")))
                .andExpect(jsonPath("$.username", is("navid1")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.admin", is(true)))
                .andExpect(jsonPath("$.seller", is(false)));
    }

    @Test
    public void putUser() throws Exception {

        User newUser = new User("1","navid2", "", "123", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);

        mockMvc.perform(put("/users/user/1")
                        .content(mapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("navid2")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.admin", is(true)))
                .andExpect(jsonPath("$.seller", is(false)));
    }

    @Test
    public void postUser() throws Exception {
        User newUser = new User("100","navid50", "navid50", "123", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);

        mockMvc.perform(post("/users/newuser")
                        .content(mapper.writeValueAsString(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("navid50")))
                .andExpect(jsonPath("$.username", is("navid50")))
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.admin", is(true)))
                .andExpect(jsonPath("$.seller", is(false)));
    }

    @Test
    public void deleteUser() throws Exception {

        mockMvc.perform(delete("/users/user/{id}", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
