package org.example.bookmarket.service;

import org.example.bookmarket.domain.Address;
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

  // 회원과 주소 함께 저장
  public Member saveMemberWithAddress(Member member, Address address) {
    // 비밀번호 암호화
    member.setPasswd(passwordEncoder.encode(member.getPasswd()));
    
    // Address에 Member 설정
    address.setMember(member);
    
    // Member에 Address 설정
    member.setAddress(address);
    
    // Member만 저장하면 Address도 함께 저장됨 (cascade)
    return memberRepository.save(member);
  }

  // 회원의 주소 정보 조회 예시
  public String getMemberFullAddress(String memberId) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));
    
    Address address = member.getAddress();
    
    if (address != null) {
      String fullAddress = address.getAddressname() + " " + address.getDetailname();
      return fullAddress;
    }
    
    return null;
  }
}
