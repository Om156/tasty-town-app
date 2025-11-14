// package com.tastytown.security.jwt;

// import java.io.IOException;

// import org.springframework.http.ProblemDetail;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.tastytown.entity.UserEntity;
// import com.tastytown.repository.UserRepository;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class JwtFilter extends OncePerRequestFilter {

//     private final JwtUtil jwtUtil;
//     private final ObjectMapper objectMapper;
//     private final UserRepository userRepository;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//             HttpServletResponse response,
//             FilterChain filterChain) throws ServletException, IOException {

//         var path = request.getRequestURI();
//         System.out.println(path);

//         if (path.contains("login") ||
//                 path.contains("register") ||
//                 path.contains("images") ||
//                 // path.startsWith("/api/orders") || // ✅ Add this line
//                 (path.startsWith("/api/categories") && request.getMethod().equals("GET")) ||
//                 (path.startsWith("/api/foods") && request.getMethod().equals("GET"))) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         final String authHeader = request.getHeader("Authorization");

//         try {
//             if (authHeader == null) {
//                 throw new RuntimeException("token should not be empty");
//             }

//             String jwt = authHeader.substring(7);
//             String userId = jwtUtil.extractUsername(jwt);

//             if (userId == null || userId.isEmpty() || userId.isBlank())
//                 throw new RuntimeException("Token not found");

//             if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//                 UserEntity user = userRepository.findById(userId).orElseThrow();

//                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                         user.getUserEmail(), user.getUserPassword());

//                 SecurityContextHolder.getContext().setAuthentication(authToken);
//                 request.setAttribute("userId", userId);
//             }

//             filterChain.doFilter(request, response);
//         } catch (Exception e) {
//             var problemDetails = ProblemDetail.forStatus(400);
//             problemDetails.setTitle("Token issue");
//             problemDetails.setDetail(e.getMessage());

//             response.setContentType("application/json");
//             response.setStatus(400);
//             response.getWriter().println(objectMapper.writeValueAsString(problemDetails));
//         }

//     }
// }


package com.tastytown.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastytown.entity.UserEntity;
import com.tastytown.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response); // Let Spring Security handle unauthorized access
                return;
            }

            String jwt = authHeader.substring(7);
            String userId = jwtUtil.extractUsername(jwt);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserEntity user = userRepository.findById(userId).orElseThrow();

                 // Build authorities from user's role
                var authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));

                // Create authentication token
                var authToken = new UsernamePasswordAuthenticationToken(user, null, authorities);

                // Set authentication and userId for downstream access
                SecurityContextHolder.getContext().setAuthentication(authToken);
                request.setAttribute("userId", userId);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            var problemDetails = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED.value());
            problemDetails.setTitle("Invalid Token");
            problemDetails.setDetail(e.getMessage());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(objectMapper.writeValueAsString(problemDetails));
        }
    }
}
