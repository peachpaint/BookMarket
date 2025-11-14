package com.springboot.controller;

import com.springboot.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import com.springboot.domain.Book;

import java.util.List;

@Controller
public class BookController {
  @Autowired
  private BookService bookService;
  @RequestMapping(value = "/books", method = RequestMethod.GET)
  public String requestBook(Model model) {
    List<Book> list = bookService.getAllBookList();
    model.addAttribute("bookList",list);
    return "books";
  }//@RequestMapping : 컨트롤러의 웹 요청을 처리할 메서드를 매핑하은 에노테이션이다
  //ex) http://locallhost:8080/books를 입력하면 ("/books")으로 매핑된다
}
