package com.akb.codeanlyzer.controller.filter;

import com.akb.codeanlyzer.service.ProjectService;
import com.akb.codeanlyzer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "projectFilter", urlPatterns = {"/proj/p/*"})
public class ProjectFilter implements Filter {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ProjectService projectService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //System.out.println("filter affect : " + ((HttpServletRequest) servletRequest).getRequestURI());
        HttpSession session = ((HttpServletRequest)servletRequest).getSession();
        String token = (String) session.getAttribute("token_login");
        String userName = (String) session.getAttribute("username");
        boolean isTokenAvailable = userService.checkToken(token, userName);

        if(token == null || userName == null || !isTokenAvailable){
            ((HttpServletResponse)servletResponse).sendRedirect("/");
            return;
        }


        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        int start = uri.indexOf("proj/p/") + 7;
        int end = uri.indexOf("/", start);
        int endq = uri.indexOf("?", start);
        String  p = uri.substring(start, (end < 0 ? uri.length() : end));


        System.out.println( projectService.getAccessibleOrgProjects(userName).get(0));
        if(projectService.getIndividualProjects(userName).contains(p) ||
                projectService.getAccessibleOrgProjects(userName).contains("(" + p +"," + servletRequest.getParameter("org") + ")") )
            filterChain.doFilter(servletRequest, servletResponse);
        else
            ((HttpServletResponse)servletResponse).sendRedirect("/");
    }
}
