package com.booking.Booking.service.service;
import com.booking.Booking.service.entity.Book;
import com.booking.Booking.service.entity.Customer;
import com.booking.Booking.service.repository.BookRepository;
import com.booking.Booking.service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BookRepository bookRepository;

    public Customer createCustomer(Customer customer) {
        if (customer.getBooks() != null) {
            for (Book book : customer.getBooks()) {
                book.setCustomer(customer);
            }
        }
        return customerRepository.save(customer);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Customer> getCustomersByBookName(String bookName) {
        List<Book> books = bookRepository.findByName(bookName);
        return books.stream()
                .map(Book::getCustomer)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Customer> getCustomersByCategory(String category) {
        List<Book> books = bookRepository.findByCategory(category);
        return books.stream()
                .map(Book::getCustomer)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Book> getBookByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    public Customer assignBookToCustomer(int customerId, Book book) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            book.setCustomer(customer);
            bookRepository.save(book);
            customer.getBooks().add(book);
            customerRepository.save(customer);
        }
        return customer;
    }
}