package IOT_house.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import IOT_house.filter.AdminFilter;
import IOT_house.filter.AuthenticationFilter;
import jakarta.servlet.Filter;

@Configuration
public class FilterConfig {
	
	@Bean
    public FilterRegistrationBean<Filter> adminFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminFilter());
        registrationBean.addUrlPatterns("/admin/*"); // Định nghĩa URL được áp dụng
        registrationBean.setOrder(2); // Đặt thứ tự ưu tiên của Filter
        return registrationBean;
    }
	
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/user/*", "/admin/*"); // Áp dụng cho /user/* và /admin/*
        registrationBean.setOrder(1); // Thứ tự ưu tiên của filter
        return registrationBean;
    }

}
