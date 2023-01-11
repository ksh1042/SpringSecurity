package com.roman14.security.controller;

import com.roman14.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController
{
  private final MemberRepository memberRepository;

  @GetMapping("/form")
  public String loginGet()
  {
    return "login/loginForm";
  }

}