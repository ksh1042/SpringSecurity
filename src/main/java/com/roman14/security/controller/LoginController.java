package com.roman14.security.controller;

import com.roman14.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController
{
  private final MemberRepository memberRepository;

  @GetMapping("/loginForm")
  public String loginGet()
  {
    return "login/loginForm";
  }

}