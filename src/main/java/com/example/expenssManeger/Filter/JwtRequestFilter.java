package com.example.expenssManeger.Filter;

import com.example.expenssManeger.service.AppuserDetail;
import com.example.expenssManeger.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final AppuserDetail appUserDetailService;
    private final JwtUtil jwtUtil;

    /**
     * ✅ Skip JWT filter for public & preflight endpoints
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return HttpMethod.OPTIONS.matches(request.getMethod())
                || path.startsWith("/login")
                || path.startsWith("/encode")
                || path.startsWith("/register")
                || path.startsWith("/activate")
                || path.startsWith("/health")
                || path.startsWith("/email"); // ✅ IMPORTANT
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String email = null;

        // 🔐 Extract JWT
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            email = jwtUtil.extractUsername(jwt);
        }

        // 🔐 Validate JWT
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = appUserDetailService.loadUserByUsername(email);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
