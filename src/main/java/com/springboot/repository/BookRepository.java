package com.springboot.repository;

import java.util.List;
import com.springboot.domain.Book;

public interface BookRepository {
  List<Book> getAllBookList();
}
