package com.gym.config;

import com.gym.enums.Role;
import com.gym.security.UserPrincipal;
import com.gym.util.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if (request.getRequestURI().startsWith("/api/auth")) {
            TenantContext.clear();
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {

            if (principal.getRole() == Role.SUPERADMIN) {
                TenantContext.setTenant("public");
            } else {
                TenantContext.setTenant(principal.getTenant());
            }

            log.debug("Tenant resolved: {}", TenantContext.getTenant());
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        TenantContext.clear();
    }
}


