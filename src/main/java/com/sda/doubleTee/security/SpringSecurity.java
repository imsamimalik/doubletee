package com.sda.doubleTee.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // configure SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/register/**").permitAll()
//                .antMatchers("/").hasRole("ADMIN")
                .antMatchers("/courses/add").hasRole("ADMIN")
                .antMatchers("/rooms/add").hasRole("ADMIN")
                .antMatchers("/rooms/empty").hasAnyRole("ADMIN","FACULTY","STUDENT")
                .antMatchers("/teachers/empty").hasAnyRole("ADMIN","FACULTY")
                .antMatchers("/teachers/add").hasRole("ADMIN")
                .antMatchers("/timetable/add").hasRole("ADMIN")
                .antMatchers("/timetable/download").hasAnyRole("ADMIN","FACULTY")
                .antMatchers("/courses/register").hasRole("STUDENT")
                .antMatchers("/**/delete/").hasRole("ADMIN")
                .and()
                .formLogin(
                        form -> form
                                .loginPage("/login/student")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/my-timetable",true)
                                .permitAll()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login/faculty")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/my-timetable",true)
                                .permitAll()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login/admin")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .deleteCookies("JSESSIONID")
                                .invalidateHttpSession(true)
                                .permitAll()

                );
        return http.build();
    }

}