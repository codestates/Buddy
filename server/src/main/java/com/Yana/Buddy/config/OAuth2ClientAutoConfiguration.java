package com.Yana.Buddy.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
// SecurityAutoConfiguration.class 은 보안을 자동 구성해주는 class이다
// @AutoconfigureBefore(클래스) 현재 클래스가 실행되기전에 괄호 안에 있는 클래스가 실행이된다.
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class OAuth2ClientAutoConfiguration {
}
