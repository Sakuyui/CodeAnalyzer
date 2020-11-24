package com.akb.codeanlyzer.controller.filter;

import com.akb.codeanlyzer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "loginStateFilter", urlPatterns = {"/org/*", "/proj/*", "/upload/*"})
public class LoginStateFilter implements Filter {
    @Autowired
    UserServiceImpl userService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter affect : " + ((HttpServletRequest) servletRequest).getRequestURI());
        HttpSession session = ((HttpServletRequest)servletRequest).getSession();
        String token = (String) session.getAttribute("token_login");
        String userName = (String) session.getAttribute("username");
        boolean isTokenAvailable = userService.checkToken(token, userName);
        //System.out.println("token = " + token + " uName = " + userName + " " + isTokenAvailable);

        if(token == null || userName == null || !isTokenAvailable){
            ((HttpServletResponse)servletResponse).sendRedirect("/");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
