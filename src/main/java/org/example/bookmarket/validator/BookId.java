package org.example.bookmarket.validator;

import jakarta.validation.Constraint;
import java.lang.annotation.*;

@Constraint(validatedBy = BookIdValidator.class)
@Target({ElementType.MODULE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BookId {
  String message() default "{BookId.book.bookId}";
  Class<?>[] groups() default{};
  Class<?>[] payload() default{};
}
