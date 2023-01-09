package com.roman14.security.repository;

import com.roman14.security.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>
{
  Optional<Member> findByUserId(String userId);
}
