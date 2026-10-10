package com.globalside.codingchallenge.rbac;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    
    @Test 
    void adminCanGetProductsWithBasicAuth() throws Exception {
        mockMvc.perform(get("/products").with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk());
    }

    @Test 
    void userCanGetProductsWithBasicAuth() throws Exception {
        mockMvc.perform(get("/products").with(httpBasic("user", "user123")))
                .andExpect(status().isOk());
    }

    @Test 
    void otherUserCannotGetProductsWithBasicAuth() throws Exception {
        mockMvc.perform(get("/products").with(httpBasic("meow", "meow123")))
                .andExpect(status().isUnauthorized());
    }

    @Test 
    void userCannotCreateProductsWithBasicAuth() throws Exception {
        mockMvc.perform(post("/products").with(httpBasic("user", "user123"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"meow123\", \"price\": 123.45, \"description\": \"meow123\", \"color\": \"meow123\", \"category\": \"cats\", \"brand\": \"meow123\", \"currency\": \"USD\" }"))
                .andExpect(status().isForbidden());
    }

    @Test 
    void userCannotUpdateProductsWithBasicAuth() throws Exception {
        mockMvc.perform(put("/products/1").with(httpBasic("user", "user123"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"meow123\", \"price\": 123.45, \"description\": \"meow123\", \"color\": \"meow123\", \"category\": \"cats\", \"brand\": \"meow123\", \"currency\": \"USD\" }"))
                .andExpect(status().isForbidden());
    }

    @Test 
    void userCannotDeleteProductsWithBasicAuth() throws Exception {
        mockMvc.perform(delete("/products/1").with(httpBasic("user", "user123")))
            .andExpect(status().isForbidden());
    }

    @Test 
    void noAuthenticatedUserCannotGetProducts() throws Exception {
        mockMvc.perform(get("/products"))
            .andExpect(status().isUnauthorized());
    }

    // i have notice that the test are using same h2 persistent database.
    // so i did disabled this test to prevent unintended change to development data.
    // that's why i didn't also write test for post method or delete method.

    // @Test
    // void adminCanUpdateProductsWithBasicAuth() throws Exception {
    //     mockMvc.perform(put("/products/34").with(httpBasic("admin", "admin123"))
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content("{\"name\": \"meow123\", \"price\": 123.45, \"description\": \"meow123\", \"color\": \"meow123\", \"category\": \"cats\", \"brand\": \"meow123\", \"currency\": \"USD\" }"))
    //             .andExpect(status().isOk());
    // }

}