package org.example.bookmarket.controller;

import org.example.bookmarket.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.example.bookmarket.domain.Book;

import java.util.List;

@Controller
@RequestMapping(value = "/books")
public class BookController {
//@RequestMapping : 컨트롤러의 웹 요청을 처리할 메서드를 매핑하은 에노테이션이다
//ex) http://locallhost:8080/books를 입력하면 ("/books")으로 매핑된다

  @Autowired// 클래스의 프로퍼티(멤버 변수)에 선언
  private BookService bookService;
  //@Autowired : 스프링 컨테이너에 등록된 빈(Bean) 중에서 타입이 같은 빈을 찾아 자동으로 의존 관계를 주입해주는 어노테이션

  @GetMapping
  public String requestBookList(Model model) {
    List<Book> list = bookService.getAllBookList();
    model.addAttribute("bookList", list);
    return "books";
  }// GET /books

  @GetMapping("/{bookId}")
  public String requestBookById(@PathVariable("bookId") String bookId, Model model) {
    Book bookById = bookService.getBookById(bookId);
    model.addAttribute("book", bookById);
    return "book";
  }// GET /books/{bookId}

  @GetMapping("/category/{category}")
  public String requestBookListByCategory(
      @PathVariable("category") String category, Model model) {
    List<Book> booksByCategory = bookService.getBookListByCategory(category);
    model.addAttribute("bookList", booksByCategory);
    model.addAttribute("category", category);
    return "books";
  }

  @GetMapping("/add")
  public String requestAddBookForm(Model model){
    model.addAttribute("book", new Book());
    return "addBook";
  }

  @PostMapping("/add")
  public String submitAddBookForm(@ModelAttribute Book book){
    bookService.setNewBook(book);
    return "redirect:/books";//웹 요청 url을 /books 로 강제 이동시켜 @RequestMapping(value = "/books")에 매핑 시킴
  }

  @ModelAttribute
  public void  addAttributes(Model model){
    model.addAttribute("addTitle", "신규 도서 등록");
  }

}