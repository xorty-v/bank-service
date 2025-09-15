package com.example.bankcards.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Filter for JWT-based authentication.
 * Intercepts each HTTP request, extracts JWT token from the Authorization header,
 * validates it, and sets the authentication in the SecurityContext.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailService userDetailService;

    /**
     * Filters each HTTP request to authenticate user based on JWT token.
     *
     * @param req   the ServletRequest
     * @param resp  the ServletResponse
     * @param chain the FilterChain
     * @throws IOException      if an input/output error happens
     * @throws ServletException if a servlet error happens
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) req;

        String token = getJwtFromRequest(httpRequest);
        if (token != null && jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUsernameFromToken(token);
            var userDetails = userDetailService.loadUserByUsername(username);

            var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(req, resp);
    }

    /**
     * Extracts JWT token from Authorization header.
     *
     * @param request the HttpServletRequest
     * @return the JWT token string if present, null otherwise
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
