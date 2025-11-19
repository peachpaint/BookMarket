package org.example.bookmarket.domain;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MemberIdValidator implements ConstraintValidator<MemberId, String> {
  //ConstraintValidator 인터페이스는 매개변수 두개를 정의
  @Override
  public void initialize(MemberId constraintAnnotation) {
  //initialize() : 사용자 정의 에너테이션과 관련된 정보를 읽어 초기화한다
    ConstraintValidator.super.initialize(constraintAnnotation);
  }


  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
  //isValid() : 유효성 검사 로직 수행, value-> 유효성 검사를 위한 도메인 클래스의 멤버 변수 값
    if(value.equals("admin")){
      return false;
    }
    return true;
  }
}
