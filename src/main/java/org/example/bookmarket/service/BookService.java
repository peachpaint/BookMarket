package org.example.bookmarket.service;

import java.util.List;
import org.example.bookmarket.domain.Book;

public interface BookService {
  List<Book> getAllBookList();
  Book getBookById(String bookId);
  List<Book> getBookListByCategory(String category);
  void setNewBook(Book book);
}
