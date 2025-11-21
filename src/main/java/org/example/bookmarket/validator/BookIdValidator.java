package org.example.bookmarket.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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
      bookService.getBookById(value);
      // 책이 존재하면 중복이므로 false 반환
      return false;
    } catch (org.example.bookmarket.BookIdException e) {
      // ID가 존재하지 않으면(예외 발생) 신규 등록 가능
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
