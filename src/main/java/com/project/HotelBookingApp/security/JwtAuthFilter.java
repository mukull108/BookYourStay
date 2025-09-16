package com.project.HotelBookingApp.security;

import com.project.HotelBookingApp.entities.User;
import com.project.HotelBookingApp.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;


    //this will help to pass exception from filter context to servlet context
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver; //filed injection will only work

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{

            final String requestTokenHeader = request.getHeader("Authorization");

            if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer") ){
                filterChain.doFilter(request,response); //call next filter in filter chain
                return;
            }

            String token = requestTokenHeader.split("Bearer ")[1]; //["", Token] array
            Long userIdFromToken = jwtService.getUserIdFromToken(token);


            //if you don't have authentication available in your security context
            if(userIdFromToken != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user = userService.getUserById(userIdFromToken);
                //check if user should be allowed
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                //other; this will contain details related to user device, ex: ip address
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request,response);//request will go to next filter in filter chain

            //perform something on the response

        }catch (Exception e){
            //all the exceptions will be moved to servlet here
            handlerExceptionResolver.resolveException(request,response,null,e);
        }

    }
}
