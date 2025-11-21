package org.example.bookmarket.repository;

import org.example.bookmarket.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class MemberRepository {
  Optional<Member> findByMemberId(String memberId);
  boolean existsByMemberId(String memberId);
}
