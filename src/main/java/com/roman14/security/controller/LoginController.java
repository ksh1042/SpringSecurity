package com.roman14.security.controller;

import com.roman14.security.config.auth.PrincipalDetails;
import com.roman14.security.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController
{
  private final MemberRepository memberRepository;

  @GetMapping("/form")
  public String loginGet()
  {
    return "login/loginForm";
  }

  @GetMapping("/test")
  @ResponseBody
  public String loginTestGet(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User)
  {
    log.info("authentication.getPrincipal type : {}", authentication.getPrincipal().getClass().getCanonicalName());
    log.info("authentication.getPrincipal : {}", authentication.getPrincipal());
    log.info("oAuth2User.getAttributes : {}", oAuth2User.getAttributes());

    return "please check console";
  }

}