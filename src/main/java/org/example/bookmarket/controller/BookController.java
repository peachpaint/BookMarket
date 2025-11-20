package org.example.bookmarket.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.bookmarket.service.BookService;
import org.example.bookmarket.validator.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Locale;
import org.example.bookmarket.domain.Book;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

  @Value("${file.uploadDir}")
  String fileDir;

  @Autowired
  private BookValidator bookValidator;
//  private UnitsInStockValidator unitsInStockValidator;

  @Autowired
  private MessageSource messageSource;

  @GetMapping("/add")
  public String requestAddBookForm(Model model){
    model.addAttribute("book", new Book());
    return "addBook";
  }

  @PostMapping("/add")
  public String submitAddBookForm(@Validated @ModelAttribute Book book, BindingResult bindingResult){
    if(bindingResult.hasErrors()){
      return "addBook";
    }
    MultipartFile bookImage = book.getBookimage();
    String saveName = bookImage.getOriginalFilename();
    File saveFile = new File(fileDir,saveName);
    if(bookImage!=null && !bookImage.isEmpty()){
      try{
        bookImage.transferTo(saveFile);
      }catch(Exception e){
        throw new RuntimeException("도서 이미지 업로드가 실패하였습니다",e);
      }
    }
    book.setFileName(saveName);
    bookService.setNewBook(book);
    return "redirect:/books";//웹 요청 url을 /books 로 강제 이동시켜 @RequestMapping(value = "/books")에 매핑 시킴
  }

  @GetMapping("/_debug/message")
  @ResponseBody
  public String debugMessage(Locale locale) {
    // Use the overload with a default message to avoid NoSuchMessageException
    String code = "addBook.form.title.label";
    String val = messageSource.getMessage(code, null, "[MISSING]", locale);
    return "locale=" + locale + " => " + val;
  }

  @GetMapping("/download")
  public void downloadBookImage(@RequestParam("file") String paramKey,
                                HttpServletResponse response) throws IOException {
    File imageFile = new File(fileDir,paramKey);
    response.setContentType("application/download");//setContentType : 다운로드를 위한 타입 설정
    response.setContentLength((int) imageFile.length());//setContentLength : 다운로드 파일 크기 설정
    response.setHeader("Content-Disposition", "attachment; filename=\"" + paramKey + "\"");
    OutputStream os = response.getOutputStream();//setContentLength : 서버로 부터 파일 다운
    FileInputStream fis = new FileInputStream(imageFile);//FileInputStream() : 파일 입력 객체를 생성
    FileCopyUtils.copy(fis, os);
    fis.close();
    os.close();
  }

  @ModelAttribute
  public void  addAttributes(Model model){
    model.addAttribute("addTitle", "신규 도서 등록");
  }

}