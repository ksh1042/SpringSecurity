package com.roman14.security;

import com.roman14.security.entity.Member;
import com.roman14.security.entity.enume.Role;
import com.roman14.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringSecurityApplication
{
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final MemberRepository memberRepository;

  public static void main(String[] args)
  {
    SpringApplication.run(SpringSecurityApplication.class, args);
  }

  @Bean
  public ApplicationRunner applicationRunner()
  {
    return args -> {
      init();
    };
  }

  private void init()
  {
    Member member1 = getMember("user01", bCryptPasswordEncoder.encode("user01"), Role.ROLE_ADMIN);
    Member member2 = getMember("user02", bCryptPasswordEncoder.encode("user02"), Role.ROLE_MANAGER);
    Member member3 = getMember("user03", bCryptPasswordEncoder.encode("user03"), Role.ROLE_USER);
    Member member4 = getMember("user04", bCryptPasswordEncoder.encode("user04"), Role.ROLE_USER);

    memberRepository.save(member1);
    memberRepository.save(member2);
    memberRepository.save(member3);
    memberRepository.save(member4);
  }

  private Member getMember(String userId, String password, Role role)
  {
    return Member.builder()
      .userId(userId)
      .password(password)
      .role(role)
      .addTime(LocalDateTime.now())
      .isEnabled(true)
      .isExpired(false)
      .isLocked(false)
      .build();
  }
}
