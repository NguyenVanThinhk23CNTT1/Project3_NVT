package comk23cnt1.nvt.project3.nvt_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class NvtSecurityConfig {

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
                                "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()

                        // login page permit
                        .requestMatchers("/admin/login").permitAll()

                        // admin protect
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // others
                        .anyRequest().permitAll()
                )

                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout=true")
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
