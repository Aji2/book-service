package com.booking.Booking.service.repository;

import com.booking.Booking.service.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findByName(String name);
    List<Book> findByCategory(String category);
}
