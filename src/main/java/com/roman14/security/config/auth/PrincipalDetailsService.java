package com.roman14.security.config.auth;

import com.roman14.security.entity.Member;
import com.roman14.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService
{
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    Optional<Member> findMember = memberRepository.findByUserId(username);

    log.info("try login : {} {}", username, findMember.isPresent());

    if(findMember.isPresent())
      return new PrincipalDetails(findMember.get());
    else
      return null;
  }
}
