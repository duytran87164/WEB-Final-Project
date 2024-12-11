package IOT_house.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import IOT_house.services.CustomUserDetailsService;
import IOT_house.services.user.impl.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public UserDetailsService userDetailsService() {
    	return new UserService();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService)
			.passwordEncoder(passwordEncoder());
	}
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();

        configurers.add(new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(customUserDetailsService);
            }
        });
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/home/**", "/assets/**").permitAll()
                .requestMatchers("/user/**","/user/profile/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                .requestMatchers("/api/**").permitAll()
            )
            .formLogin(login -> login
                .loginPage("/home/login")
                .loginProcessingUrl("/home/login")
                .defaultSuccessUrl("/home/waiting", true)
                .failureUrl("/home/login?error=Invalid User or Password")
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/home/logout")
                    .logoutSuccessUrl("/home/")
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll() )
            .exceptionHandling(handling -> handling.accessDeniedPage("/403"))
            .build();
    }
}
