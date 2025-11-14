package com.springboot.repository;

import com.springboot.domain.Book;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository{
  private List<Book> listOfBooks = new ArrayList<Book>();
  public BookRepositoryImpl(){
    Book book1 = new Book();
    book1.setBookId("ISBN1234");
    book1.setName("Book 1");
    book1.setUnitPrice(new BigDecimal(30000));
    book1.setAuthor("윤진주1");
    book1.setDescription(
        "기본 예시 1번 입니다"
    );
    book1.setPublisher("출판사1");
    book1.setCategory("IT전문서");
    book1.setUnitsInStock(1000);
    book1.setReleaseDate("2025.11.14");

    Book book2 = new Book();
    book2.setBookId("ISBN5678");
    book2.setName("Book 2");
    book2.setUnitPrice(new BigDecimal(29800));
    book2.setAuthor("윤진주2");
    book2.setDescription(
        "기본 예시 2번 입니다"
    );
    book2.setPublisher("출판사2");
    book2.setCategory("IT전문서");
    book2.setUnitsInStock(1000);
    book2.setReleaseDate("2025.11.14");

    Book book3 = new Book();
    book3.setBookId("ISBN5678");
    book3.setName("Book 3");
    book3.setUnitPrice(new BigDecimal(36000));
    book3.setAuthor("윤진주3");
    book3.setDescription(
        "기본 예시 2번 입니다"
    );
    book3.setPublisher("출판사2");
    book3.setCategory("IT전문서");
    book3.setUnitsInStock(1000);
    book3.setReleaseDate("2025.11.14");

    listOfBooks.add(book1);
    listOfBooks.add(book2);
    listOfBooks.add(book3);
  }
  public List<Book> getAllBookList(){
    return listOfBooks;
  }//저장된 몯든 도서 목록을 가져오는 메서드
}
