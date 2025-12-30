package comk23cnt1.nvt.project3.nvt_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable @PreAuthorize, @Secured annotations
public class NvtSecurityConfig {

        private final comk23cnt1.nvt.project3.nvt_security.NvtAccessDeniedHandler accessDeniedHandler;

        public NvtSecurityConfig(comk23cnt1.nvt.project3.nvt_security.NvtAccessDeniedHandler accessDeniedHandler) {
                this.accessDeniedHandler = accessDeniedHandler;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                // dev nhanh: để POST form không bị 403 khi chưa dùng csrf token
                                .csrf(csrf -> csrf.disable())

                                .authorizeHttpRequests(auth -> auth
                                                // public pages
                                                .requestMatchers(
                                                                "/", "/rooms/**", "/bills", "/feedback/**",
                                                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                                                "/access-denied")
                                                .permitAll()

                                                // login and register pages
                                                .requestMatchers("/login", "/register").permitAll()

                                                // admin only
                                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                                // user and admin can access
                                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                                                // api endpoints require authentication
                                                .requestMatchers("/api/**").authenticated()

                                                // others
                                                .anyRequest().permitAll())

                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/user/home", true) // Tất cả user đều về /user/home
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true"))

                                // Access denied handler
                                .exceptionHandling(ex -> ex
                                                .accessDeniedHandler(accessDeniedHandler))

                                .httpBasic(Customizer.withDefaults());

                return http.build();
        }
}
