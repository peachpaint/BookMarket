package org.example.bookmarket.validator;

import jakarta.validation.ConstraintViolation;
import org.example.bookmarket.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashSet;
import java.util.Set;

public class BookValidator implements Validator {
  @Autowired
  private jakarta.validation.Validator beanValidator;
  public Set<Validator> springValidators;
  public BookValidator() {
    springValidators = new HashSet<Validator>();
  }
  public void setSpringValidators(Set<Validator> springValidators) {
    this.springValidators = springValidators;
  }
  @Override
  public boolean supports(Class<?> clazz) {
    return Book.class.isAssignableFrom(clazz);
  }
  @Override
  public void validate(Object target, Errors errors) {
    Set<ConstraintViolation<Object>> violations = beanValidator.validate(target);
    for (ConstraintViolation<Object> violation : violations) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.rejectValue(propertyPath,null,message);
      //errors.reject() : 전역 에러 메서드, Thymeleaf의 th:errors="*{bookId}" 같은 필드별 에러 표시가 작동하지 않음
      //errors.rejectValue() : 필드별로 에러를 등록하여 HTML에서 th:errors로 표시 가능
    }
    for(Validator validator : springValidators){
      validator.validate(target, errors);
    }
  }
}
