# Spring Security
- 스프링 시큐리티 기록 및 연습용 리퍼지토리

## 1. 초기 설정
### 1.1 라이브러리 의존성 추가
- gradle의 경우 아래 처럼 시큐리티 의존성을 추가할 수 있다.
```groovy
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-security'
}
```

---

## 2. 커스텀 설정 활성법
### 2.1 EnableWebSecurity 
- @EnableWebSecurity 설정을 통해 스프링 시큐리티의 기본 설정을 대체하는 클래스를 생성할 수 있다.
```java
@Configuration
@EnableWebSecurity
public class DefaultSecurityConfig {
  //
}
```
- 해당 어노테이션이 지정된 클래스가 생성되면 스프링 시큐리티가 제공하는 기본적인 기능들도 같이 비활성화가 된다.
> ex) ```/login```, ```/logout``` 등 스프링 부트에서 기본적으로 제공해주는 URI 및 템플릿

---

### 2.2. SecurityFilterChain Bean
- ```SecurityFilterChain``` 객체를 반환하는 빈을 만들어 필터체인을 구현할 수 있다.
- **기존**(security v5.7.0-M2 이전)에는 ```WebSecurityConfigurerAdapter``` 클래스를 상속받고 메소드를 오버라이드 하여 구현하였지만 Depreacated 처리 되었으므로 빈 객체 생성으로 구현한다.
- 기존과는 달리 람다식을 통해 설정을 구현하도록 구현이 되어있다.

```java
@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록되도록 하는 어노테이션
public class SecurityConfig
{
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception
  {
    return security.build();
  }
}
```

---

## 3. 체인 설정
### 3.1. CORS 설정
#### 2.3.1. CORS 비활성화
- ```cors(cors -> cors.disable())```를 통해 비활성화 가능하다.
```java
public class SecurityConfig
{
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception
  {
    return security
      .cors(cors -> cors.disable())
      .build();
  }
}
```
### 3.2. 권한 체크
- ```...Matcher()``` = 대상 범위를 입력한 패턴으로 지정
- ```.authenticated()``` = 인증(로그인)이 되었으면 허가
- ```.access()```입력한 ROLE과 일치하면 허가
```java
public class SecurityConfig
{
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception
  {
    return security
      .authorizeRequests(request -> {
        request.antMatchers("/user/**").authenticated();
        request.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')");
        request.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
        request.anyRequest().permitAll();
      })

      .build();
  }
}
```
- 허가되지 않은 URL 방문 시 ```403``` 코드를 리턴한다.

---

### 3.3 로그인 설정
#### 3.3.1. 스프링 시큐리티 로그인 기본 설정
- ```.formLogin()```을 통해 로그인 관련 설정을 람다식으로 입력
- ```loginPage()``` = 로그인 페이지의 URL을 입력
- ```loginProcessingUrl()``` = 해당 URL을 스프링 시큐리티가 가로채 로그인 기능을 대신 수행
- ```defaultSuccessUrl()``` = 성공 시 호출할 URL을 지정
- ```usernameParameter()``` = 로그인 시 로그인 폼으로 부터 전달받을 input 파라미터 name
```java
public class SecurityConfig
{
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception
  {
    return security
      .formLogin(login -> {
        login.loginPage("/login");
        login.loginProcessingUrl("/login"); // 해당 URL은 UserDetailsService 인터페이스를 구현한 클래스로 넘어간다.
        login.defaultSuccessUrl("/user");
        login.usernameParameter("usernameXX");
      })
      .build();
  }
}
```
```html
<form>
  <!-- name을 username으로 지정하지 않는다면 로그인시 스프링 시큐리티의 UserDetailsService에서 캐치하지 못한다. -->
  <!-- name을 다른 이름으로 바꾸고 싶다면 시큐리티 설정에서 userNameParameter() 메서드를 통해 재정의 해주어야 한다. -->
  <input type="text" name="usernameXX" placeholder="userId">
  <input type="password" name="password" placeholder="password">
</form>
```
#### 3.3.2. UserDetails
- UserDetails 인터페이스를 통해 사용자 엔티티를 컴포지션하여 인가와 관련된 사용자 상태에 대한 클래스를 구현한다.
```java
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsContructor
public class PrincipalDetails implements UserDetails
{
  private final Member member;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities()
  {
    // ROLE 리스트를 GrantedAuthority 컬렉션 객체로 반환
    Collection<GrantedAuthority> gaList = List.of(
      () -> String.valueOf(member.getRole())
    );
    return gaList;
  }
  
  // ... etc override methods
}
```


#### 3.3.3. UserDetailsService
- UserDetailsService 인터페이스를 통해 구현하는 클래스로, 3.3.1 항목에서 정의한 ```loginProcessingUrl()```을 수행하는 역할을 한다. 
```java
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService
{
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    Optional<Member> findMember = memberRepository.findByUserId(username);

    if(findMember.isPresent())
      return new PrincipalDetails(findMember.get());
    else
      return null;
  }
}
```

#### 3.3.4. 기타
- 사용자가 로그인을 하면 아래와 같은 구조로 스프링 시큐리티 세션에 인증 정보가 저장된다.
>spring security Session<br>
>&nbsp;&nbsp;ㄴ spring security Authentication<br>
>&nbsp;&nbsp;&nbsp;&nbsp;ㄴ spring security UserDetails

---