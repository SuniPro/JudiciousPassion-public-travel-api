package com.suni.judiciouspassion.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/user/**", "/board", "/file/get", "/collection/**"
    };

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws Exception {
        //CSRF, CORS
//        http.csrf((csrf) -> csrf.disable());
//        http.cors(Customizer.withDefaults());
//
//        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
//        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
//                SessionCreationPolicy.STATELESS));
//
//        //FormLogin, BasicHttp 비활성화
//        http.formLogin((form) -> form.disable());
//        http.httpBasic(AbstractHttpConfigurer::disable);
//
//        // 권한 규칙 작성
//        http.authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(AUTH_WHITELIST).permitAll()
//                        //@PreAuthrization 을 사용할 것이기 때문에 모든 경로에 대한 인증처리는 Pass
//                        .anyRequest().permitAll()
////                        .anyRequest().authenticated()
//        );
//
//        return http.build();
//    }
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 권한 설정
                .authorizeExchange(authorize -> authorize
                        // 이후 AUTH_WHITELIST 정립 후 아래 로직으로 변경 예정
                                .anyExchange().permitAll()
//                        .pathMatchers(AUTH_WHITELIST).permitAll()  // 인증 없이 접근 가능
//                        .anyExchange().authenticated()             // 그 외는 인증 필요
                )
                // HTTP Basic 및 Form Login 비활성화
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }
}
