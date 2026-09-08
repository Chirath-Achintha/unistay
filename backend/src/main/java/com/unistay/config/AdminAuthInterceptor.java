package com.unistay.config;

import com.unistay.util.AdminTokenStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * HTTP Interceptor that protects all /api/admin/** routes (except /api/admin/login).
 *
 * Any request to a protected admin route must include the header:
 *   X-Admin-Token: <valid-token>
 *
 * If the token is missing, expired, or invalid, the request is rejected with HTTP 401.
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final AdminTokenStore tokenStore;

    @Autowired
    public AdminAuthInterceptor(AdminTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        String token = request.getHeader("X-Admin-Token");

        if (!tokenStore.isValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"Unauthorized: Invalid or expired admin session. Please log in again.\"}"
            );
            return false; // stop further processing
        }

        return true; // proceed to controller
    }
}
