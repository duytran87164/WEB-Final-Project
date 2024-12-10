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
        
        // Cấu hình toàn cục cho xác thực
        configurers.add(new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(AuthenticationManagerBuilder auth) throws Exception {
                // Ví dụ: thêm một cách xác thực với UserDetailsService
                auth.userDetailsService(customUserDetailsService);
                // Có thể thêm các cấu hình khác như mã hóa mật khẩu, xác thực qua cơ sở dữ liệu, v.v.
            }
        });
        return authConfig.getAuthenticationManager();
    }
    // Cấu hình SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/home/**", "/assets/**").permitAll() // Trang công cộng
                .requestMatchers("/user/**","/user/profile/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN") // Người dùng có quyền USER
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // Admin có quyền ADMIN
                .requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                .requestMatchers("/api/**").permitAll()
            )
            .formLogin(login -> login
                .loginPage("/home/login") // Trang login
                .loginProcessingUrl("/home/login") // URL xử lý đăng nhập
                .defaultSuccessUrl("/home/waiting", true) // Thành công chuyển hướng đến /home/waiting
                .failureUrl("/home/login?error=Invalid User or Password") // Nếu đăng nhập thất bại, quay lại trang login với lỗi
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/home/logout") // URL để logout
                    .logoutSuccessUrl("/home/") // Trang chuyển hướng sau khi logout thành công
                    .clearAuthentication(true) // Xóa thông tin xác thực
                    .deleteCookies("JSESSIONID") // Xóa cookie phiên
                    .permitAll() )// Cho phép tất cả người dùng thực hiện logout
            .exceptionHandling(handling -> handling.accessDeniedPage("/403")) // Truy cập bị từ chối sẽ chuyển đến /403
            .build();
    }
}
