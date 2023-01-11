package com.roman14.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HomeController
{
  @GetMapping({"", "/"})
  public String index()
  {
    return "hello world";
  }

  @GetMapping("/all")
  public String all()
  {
    return "all";
  }

  @GetMapping("/user")
  public String user()
  {
    return "user";
  }

  @GetMapping("/admin")
  public String admin()
  {
    return "admin";
  }

  @GetMapping("/manager")
  public String manager()
  {
    return "admin";
  }

  @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
  @GetMapping("/info")
  public String info()
  {
    return "user info";
  }

  @PreAuthorize("hasRole('ROLE_MANAGER') and hasRole('ROLE_ADMIN')")
  @GetMapping("/data")
  public String data()
  {
    return "data";
  }
}
