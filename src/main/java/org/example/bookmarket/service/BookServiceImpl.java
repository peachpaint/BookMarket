package org.example.bookmarket.service;

import org.example.bookmarket.BookIdException;
import org.example.bookmarket.domain.Book;
import org.example.bookmarket.repository.BookJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookServiceImpl implements BookService {
//  @Autowired
//@Autowired : 스프링 컨테이너에서 의존성 주입을 자동으로 수행하도록 지시하는 어노테이션, Setter()를 대신하여 사용
//autowired 와 setter의 차이
// : @Autowired는 스프링 컨테이너가 의존성을 주입하는 방식 자체로 필드, 생성자, 수정자(setter) 등 다양한 위치에서 사용될 수 있으며
//   setter는 수정자(Setter) 주입이라는 구체적인 의존성 주입 방법 중 하나로 @Autowired를 통해 특정 필드에 값을 할당하기 위해 사용되는 메서드
//  private BookRepository bookRepository;
  @Autowired
  private BookJpaRepository bookJpaRepository;

  public List<Book> getAllBookList(){
    return bookJpaRepository.findAll();
  }

  public Book getBookById(String bookId){
    return bookJpaRepository.findById(bookId)
        .orElseThrow(() -> new BookIdException("도서ID가 "+bookId+"인 도서를 찾을 수 없습니다"));
  }

  @Override
  public List<Book> getBookListByCategory(String category){
    return bookJpaRepository.findByCategoryIgnoreCase(category);
  }

  @Override
  public void setNewBook(Book book){
    bookJpaRepository.save(book);
  }
}
