package org.example.bookmarket.service;

import org.example.bookmarket.domain.Member;
import org.example.bookmarket.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // 회원 저장
  public Member saveMember(Member member) {
    // 비밀번호 암호화
    member.setPasswd(passwordEncoder.encode(member.getPasswd()));
    return memberRepository.save(member);
  }

  // 회원 조회
  public Member findByMemberId(String memberId) {
    return memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));
  }

  // 아이디 중복 확인
  public boolean existsByMemberId(String memberId) {
    return memberRepository.existsByMemberId(memberId);
  }
}
