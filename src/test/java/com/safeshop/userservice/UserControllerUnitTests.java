package com.safeshop.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeshop.userservice.model.User;
import com.safeshop.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.boot.test.mock.mockito.MockBean;


import static org.hamcrest.Matchers.is;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testuserbyidunit() throws Exception {
        User user1 = new User("1","navid", "navid1", "123", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);
        user1.setCreatedAt(null);
        user1.setModifiedAt(null);

        given(userRepository.findById("1")).willReturn(java.util.Optional.of(user1));

        mockMvc.perform(get("/users/user/{id}", 1))
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

        User newUser = new User("1","navid1", "navid1", "123", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);
        newUser.setCreatedAt(null);
        newUser.setModifiedAt(null);

        given(userRepository.findById("1")).willReturn(java.util.Optional.of(newUser));

        User updatedUser = new User("1","navid2", "", "123", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);

        mockMvc.perform(put("/users/user/1")
                        .content(mapper.writeValueAsString(updatedUser))
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
        newUser.setCreatedAt(null);
        newUser.setModifiedAt(null);

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

        User newUser = new User("3","navid3", "navid3", "123", Boolean.TRUE,Boolean.TRUE,Boolean.FALSE);


        given(userRepository.findById("3")).willReturn(java.util.Optional.of(newUser));

        mockMvc.perform(delete("/users/user/{id}", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
