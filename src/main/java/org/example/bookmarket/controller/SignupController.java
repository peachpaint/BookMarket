package org.example.bookmarket.controller;

import jakarta.validation.Valid;
import org.example.bookmarket.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class SignupController {
  @Autowired
  private MemberService memberService;

  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("member", new Member());
    return "signup";
  }

  @PostMapping("/member")
  public String submitForm(@Valid @ModelAttribute Member member,
                           BindingResult result,
                           Model model) {
    if (result.hasErrors()) {
      return "signup";  // 유효성 검사 실패 시 회원가입 페이지로
    }

    // 회원 정보를 DB에 저장
    memberService.saveMember(member);

    model.addAttribute("member", member);
    return "memberInformation";
  }

}
