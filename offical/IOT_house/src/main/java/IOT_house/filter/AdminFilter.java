package IOT_house.filter;

import java.io.IOException;

import IOT_house.entity.Account;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Lấy thông tin người dùng từ session
        HttpSession session = httpRequest.getSession(false); // false để tránh tạo session mới nếu không có
        if (session != null) {
            Object userObj = session.getAttribute("user");

            if (userObj instanceof Account) {
                Account user = (Account) userObj;
                // Kiểm tra quyền admin
                if (user.getIsAdmin()) {
                    // Nếu là admin, cho phép tiếp tục
                    chain.doFilter(request, response);
                    return;
                }
            }
        }
        
        // Nếu không phải admin hoặc chưa đăng nhập, chuyển hướng đến trang logout hoặc trang khác
        httpResponse.sendRedirect("/home/logout"); // Hoặc sử dụng trang lỗi như "/error/403"
    }
}
