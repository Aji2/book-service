package com.booking.Booking.service.service;
import com.booking.Booking.service.entity.Book;
import com.booking.Booking.service.entity.Customer;
import com.booking.Booking.service.repository.BookRepository;
import com.booking.Booking.service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ServiceTest {

    @InjectMocks
    private Service customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Alice");

        Book book = new Book();
        book.setId(101);
        book.setName("Java");
        book.setCustomer(customer);

        customer.setBooks(List.of(book));

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.createCustomer(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getBooks().get(0).getCustomer()).isEqualTo(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = customerService.getAllBooks();

        assertThat(result).hasSize(2);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomersByBookName() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Bob");

        Book book1 = new Book();
        book1.setName("Java");
        book1.setCustomer(customer);

        Book book2 = new Book();
        book2.setName("Java");
        book2.setCustomer(customer);

        when(bookRepository.findByName("Java")).thenReturn(List.of(book1, book2));

        List<Customer> customers = customerService.getCustomersByBookName("Java");

        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getName()).isEqualTo("Bob");
    }

    @Test
    void testGetCustomersByCategory() {
        Customer customer = new Customer();
        customer.setId(2);
        customer.setName("Tom");

        Book book1 = new Book();
        book1.setCategory("Tech");
        book1.setCustomer(customer);

        when(bookRepository.findByCategory("Tech")).thenReturn(List.of(book1));

        List<Customer> result = customerService.getCustomersByCategory("Tech");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Tom");
    }

    @Test
    void testGetBookByCategory() {
        Book book1 = new Book();
        book1.setCategory("Science");

        when(bookRepository.findByCategory("Science")).thenReturn(List.of(book1));

        List<Book> books = customerService.getBookByCategory("Science");

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getCategory()).isEqualTo("Science");
    }

    @Test
    void testAssignBookToCustomer_whenCustomerExists() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Emma");
        customer.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(1);
        book.setName("Spring");

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.assignBookToCustomer(1, book);

        assertThat(result).isNotNull();
        assertThat(result.getBooks()).contains(book);
        verify(bookRepository).save(book);
        verify(customerRepository).save(customer);
    }

    @Test
    void testAssignBookToCustomer_whenCustomerNotFound() {
        Book book = new Book();
        book.setId(1);
        book.setName("Unknown");

        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        Customer result = customerService.assignBookToCustomer(99, book);

        assertThat(result).isNull();
        verify(bookRepository, never()).save(any());
        verify(customerRepository, never()).save(any());
    }
}