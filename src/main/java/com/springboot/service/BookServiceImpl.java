package com.springboot.service;

import com.springboot.domain.Book;
import com.springboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookServiceImpl implements BookService {
  @Autowired
//@Autowired : 스프링 컨테이너에서 의존성 주입을 자동으로 수행하도록 지시하는 어노테이션, Setter()를 대신하여 사용
//autowired 와 setter의 차이
// : @Autowired는 스프링 컨테이너가 의존성을 주입하는 방식 자체로 필드, 생성자, 수정자(setter) 등 다양한 위치에서 사용될 수 있으며
//   setter는 수정자(Setter) 주입이라는 구체적인 의존성 주입 방법 중 하나로 @Autowired를 통해 특정 필드에 값을 할당하기 위해 사용되는 메서드
  private BookRepository bookRepository;
  public List<Book> getAllBookList(){
    return bookRepository.getAllBookList();
  }
}
