package org.example.bookmarket.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.bookmarket.domain.Book;
import org.example.bookmarket.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookIdValidator implements ConstraintValidator<BookId, String> {

  private final BookService bookService;

  @Autowired
  public BookIdValidator(BookService bookService) {// 생성자에 파라미터
    this.bookService = bookService;
  }
  @Override
  public void initialize(BookId constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      return true;
    }
    try {
      Book book = bookService.getBookById(value);
      // 신규 등록 시에는 ID가 존재하지 않아야 함 (중복 체크)
      return book == null;
    } catch (IllegalArgumentException e) {
      // ID가 존재하지 않으면(예외 발생) 신규 등록 가능
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
