package com.roman14.security.entity;

import com.roman14.security.entity.enume.Role;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Member
{
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true)
  private String userId;

  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  private LocalDateTime addTime;

  private boolean isExpired;

  private boolean isEnabled;

  private boolean isLocked;

  protected Member() {}

  @Builder
  protected Member(Long id, String userId, String password, Role role, LocalDateTime addTime, boolean isExpired, boolean isEnabled, boolean isLocked)
  {
    this.id = id;
    this.userId = userId;
    this.password = password;
    this.role = role;
    this.addTime = addTime;
    this.isExpired = isExpired;
    this.isEnabled = isEnabled;
    this.isLocked = isLocked;
  }
}
