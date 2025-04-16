package com.booking.Booking.service.controller;
import com.booking.Booking.service.entity.Book;
import com.booking.Booking.service.entity.Customer;
import com.booking.Booking.service.service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @WebMvcTest(Controller.class)
    public class ControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private Service service;

        @Autowired
        private ObjectMapper objectMapper;

        private Customer customer;
        private Book book;

        @BeforeEach
        void setUp() {
            customer = new Customer();
            customer.setId(1);
            customer.setName("John Doe");

            book = new Book();
            book.setId(1);
            book.setName("Java Book");
            book.setCategory("Programming");
        }

        @Test
        void testCreateCustomer() throws Exception {
            when(service.createCustomer(any(Customer.class))).thenReturn(customer);

            mockMvc.perform(post("/api/customer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customer)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("John Doe"));
        }

        @Test
        void testGetAllBooks() throws Exception {
            when(service.getAllBooks()).thenReturn(Arrays.asList(book));

            mockMvc.perform(get("/api/customer/books"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Java Book"));
        }

        @Test
        void testGetCustomersByBookName() throws Exception {
            when(service.getCustomersByBookName("Java Book")).thenReturn(Arrays.asList(customer));

            mockMvc.perform(get("/api/customer/search_by_book_name")
                            .param("bookName", "Java Book"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("John Doe"));
        }

        @Test
        void testGetCustomerByCategory() throws Exception {
            when(service.getCustomersByCategory("Programming")).thenReturn(List.of(customer));

            mockMvc.perform(get("/api/customer/search_customer_by_category")
                            .param("category", "Programming"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("John Doe"));
        }

        @Test
        void testSearchBookyByCategory() throws Exception {
            when(service.getBookByCategory("Programming")).thenReturn(List.of(book));

            mockMvc.perform(get("/api/customer/search_books_by_category")
                            .param("category", "Programming"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Java Book"));
        }

        @Test
        void testAssignBookToCustomer_Success() throws Exception {
            when(service.assignBookToCustomer(eq(1), any(Book.class))).thenReturn(customer);

            mockMvc.perform(post("/api/customer/1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("John Doe"));
        }

        @Test
        void testAssignBookToCustomer_NotFound() throws Exception {
            when(service.assignBookToCustomer(eq(2), any(Book.class))).thenReturn(null);

            mockMvc.perform(post("/api/customer/2/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isNotFound());
        }
    }

