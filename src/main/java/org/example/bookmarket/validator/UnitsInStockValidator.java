package org.example.bookmarket.validator;

import org.example.bookmarket.domain.Book;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.math.BigDecimal;


@Component
public class UnitsInStockValidator implements Validator{
  public boolean supports(Class<?> Clazz) {
    return Book.class.isAssignableFrom(Clazz);
  }

  public void validate(Object target, Errors errors) {
    Book book = (Book) target;
    if(book.getUnitPrice()!=null && book.getUnitPrice().intValue()>10000 && book.getUnitsInStock()>500){
      errors.rejectValue("unitsInStock","UnitsInStockValidator.message");
    }
  }
}
