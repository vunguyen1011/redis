package com.JPA.redis.Security;

import com.JPA.redis.Exception.ErrorCode;
import com.JPA.redis.Exception.WebException;
import com.JPA.redis.Service.JwtService;
import com.JPA.redis.Service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Lấy header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;
        // Kiểm tra header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy JWT token
        jwtToken = authHeader.substring(7);
        String jti = jwtService.extractTokenId(jwtToken);
        if(tokenService.isTokenBlacklisted(jti)) {
            throw new WebException(ErrorCode.TOKEN_REVOKED);
        }

        try {
            // Lấy username từ token
            username = jwtService.extractUsername(jwtToken);

            // Kiểm tra nếu chưa xác thực
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Kiểm tra token hợp lệ
                if (jwtService.isTokenValid(jwtToken, username)) {
                    // Lấy quyền từ token
                    List<SimpleGrantedAuthority> authorities = jwtService.extractPermision(jwtToken).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // Tạo UserDetails
                    UserDetails userDetails = User.builder()
                            .username(username)
                            .password("") // Không cần mật khẩu
                            .authorities(authorities)
                            .build();

                    // Tạo authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities
                    );

                    // Thiết lập chi tiết request
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Lưu vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Xử lý token không hợp lệ
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
            return;
        }

       
        filterChain.doFilter(request, response);
    }
}