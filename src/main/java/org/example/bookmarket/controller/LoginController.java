package org.example.bookmarket.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.bookmarket.domain.Member;
import org.example.bookmarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class LoginController {
  @Autowired  // 추가
  private MemberService memberService;

  @GetMapping("/login")
  public String login(){
    return "login";
  }

  @GetMapping("/loginfailed")
  public String loginfailed(Model model){
    model.addAttribute("error", "true");
    return "login";
  }

  @GetMapping("/logout")
  public String logout(Model model){
    return "login";
  }

  @GetMapping("/memberInfo")
  public String memberInfo(Model model) {
    // 현재 로그인한 사용자의 username 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    // DB에서 회원 정보 조회
    Member member = memberService.findByMemberId(username);
    model.addAttribute("member", member);

    return "memberInformation";
  }
}
