package org.example.bookmarket.validator;

import org.example.bookmarket.domain.Book;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.math.BigDecimal;


@Component
public class UnitsInStockValidator implements Validator{
  @Override
  public boolean supports(@NonNull Class<?> Clazz) {
    return Book.class.isAssignableFrom(Clazz);
  }

  @Override
  public void validate(@NonNull Object target, @NonNull Errors errors) {
    Book book = (Book) target;
    if(book.getUnitPrice()!=null && book.getUnitPrice().intValue()>10000 && book.getUnitsInStock()>500){
      errors.rejectValue("unitsInStock","UnitsInStockValidator.message");
    }
  }
}
