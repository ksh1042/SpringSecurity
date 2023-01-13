package com.roman14.security.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService
{
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException
  {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    log.info("userRequest : {}", userRequest);
    log.info("getClientRegistration : {}", userRequest.getClientRegistration());
    log.info("getAccessToken.type : {}", userRequest.getAccessToken().getTokenType().getValue());
    log.info("getAccessToken.value : {}", userRequest.getAccessToken().getTokenValue());
    log.info("getAdditionalParameters : {}", userRequest.getAdditionalParameters());
    log.info("user.getAttributes : {}", oAuth2User.getAttributes());

    return oAuth2User;
  }
}
