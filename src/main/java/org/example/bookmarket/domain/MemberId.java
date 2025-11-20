package org.example.bookmarket.domain;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MemberIdValidator.class)//@Constraint : 도메인 객체에 부여되는 제약을 생성하는 에너테이션
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberId {
  String message() default "중복된 아이디입니다";
  //@Constraint 사용시 필수 작성_message() : 유효성 검사에서 오류가 발생하면 반환되는 메세지
  Class<?>[] groups() default {};
  //@Constraint 사용시 필수 작성_groups() : 유효성 검사를 수행하는 그룹 설정
  Class<?>[] payload() default {};
  //@Constraint 사용시 필수 작성_payload() : 유효서 검사를 수행하는 클라이언트가 사용하는 메타 데이터를 설정
}
