package comk23cnt1.nvt.project3.nvt_security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom handler xử lý khi user không có quyền truy cập
 */
@Component
public class NvtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            String username = auth.getName();
            String requestedUrl = request.getRequestURI();

            System.out.println(">>> Access Denied: User '" + username +
                    "' attempted to access: " + requestedUrl);
        }

        // Redirect đến trang access denied hoặc trang chủ
        response.sendRedirect("/access-denied");
    }
}
