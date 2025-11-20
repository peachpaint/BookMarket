package org.example.bookmarket.repository;

import org.example.bookmarket.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

//사용 X
@Deprecated
public interface BookRepository extends JpaRepository<Book, String> {
}
