package org.example.bookmarket.controller;

import org.example.bookmarket.domain.Member;
import org.example.bookmarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/member")
public class MemberController {
  @Autowired
  private MemberService memberService;

  @GetMapping("/info")
  public String memberInfo(Principal principal, Model model) {
    // 현재 로그인한 사용자의 아이디 가져오기
    String memberId = principal.getName();

    // 회원 정보 조회
    Member member = memberService.findByMemberId(memberId);

    // 모델에 담아서 뷰로 전달
    model.addAttribute("member", member);

    return "memberInformation";  // memberInformation.html로 이동
  }
}
