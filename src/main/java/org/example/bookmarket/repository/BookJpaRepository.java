package org.example.bookmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.example.bookmarket.domain.Book;

public interface BookJpaRepository extends JpaRepository<Book, String> {
  List<Book> findByCategoryIgnoreCase(String category);
}
