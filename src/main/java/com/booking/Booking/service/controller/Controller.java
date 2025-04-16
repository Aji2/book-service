package com.booking.Booking.service.controller;

import com.booking.Booking.service.entity.Book;
import com.booking.Booking.service.entity.Customer;
import com.booking.Booking.service.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class Controller {
    @Autowired
    Service service;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = service.createCustomer(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = service.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/search_by_book_name")
    public ResponseEntity<List<Customer>> getCustomersByBookName(@RequestParam String bookName) {
        List<Customer> customers = service.getCustomersByBookName(bookName);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/search_customer_by_category")
    public ResponseEntity<List<Customer>> getCustomerByCategory(@RequestParam String category){
        List<Customer> customers = service.getCustomersByCategory(category);
        return new ResponseEntity<>(customers,HttpStatus.OK);
    }

    @GetMapping("/search_books_by_category")
    public ResponseEntity<List<Book>> searchBookyByCategory(@RequestParam String category){
        List<Book> books = service.getBookByCategory(category);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @PostMapping("/{customerId}/books")
    public ResponseEntity<Customer> assignBookToCustomer(@PathVariable int customerId, @RequestBody Book book) {
        Customer customer = service.assignBookToCustomer(customerId, book);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
