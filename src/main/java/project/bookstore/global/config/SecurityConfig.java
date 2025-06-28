package project.bookstore.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //스프링 설정 클래스임을 나타냄
public class SecurityConfig {

    /***
     * 비밀번호 암호화를 위한 빈 등록
     * 다른 클래스에서 주입받아 사용할 수 있음
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /***
     *
     * @param http 보안 설정을 구성할 수 있게 도와주는 객체
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/css/**", "/js/**").permitAll() //해당 경로 누구나 접근 가능
                        .anyRequest().authenticated() //위에서 허용한 경로 외의 모든 요청은 로그인 필요
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/main", true)
                        .failureUrl("/login?error")
                        .permitAll()//누구나 접근 가능
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")//로그아웃 후에는 /login?logout 주소로 리다이렉트
                );
        return http.build();
    }
}
