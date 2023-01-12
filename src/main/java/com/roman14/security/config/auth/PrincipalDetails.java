package com.roman14.security.config.auth;

import com.roman14.security.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails
{
  private final Member member;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    Collection<GrantedAuthority> gaList = List.of(
      () -> String.valueOf(member.getRole())
    );
    return gaList;
  }

  @Override
  public String getPassword()
  {
    return member.getPassword();
  }

  @Override
  public String getUsername()
  {
    return member.getUserId();
  }

  @Override
  public boolean isAccountNonExpired()
  {
    return !member.isExpired();
  }

  @Override
  public boolean isAccountNonLocked()
  {
    return !member.isLocked();
  }

  @Override
  public boolean isCredentialsNonExpired()
  {
    return true;
  }

  @Override
  public boolean isEnabled()
  {
    return member.isEnabled();
  }

  public Member getMember()
  {
    return member;
  }

  @Override
  public String toString()
  {
    return "PrincipalDetails{" +
      "member=" + member +
      '}';
  }
}
