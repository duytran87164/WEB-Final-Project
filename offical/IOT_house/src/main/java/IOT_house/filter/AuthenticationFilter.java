package IOT_house.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Kiểm tra thông tin người dùng từ session
        Object user = httpRequest.getSession().getAttribute("user");

        // Nếu chưa đăng nhập, trả về lỗi 401 hoặc chuyển hướng
        if (user != null) {
            
         // Nếu đã đăng nhập, tiếp tục xử lý yêu cầu
            chain.doFilter(request, response);
            return;
        }
        else {
        	httpResponse.sendRedirect("/home/");
//        	httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Trả về lỗi 401
//            httpResponse.getWriter().write("Unauthorized: Please log in to access this resource.");
        	
        }

        
		
	}

}
