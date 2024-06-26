package org.aa2.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("idUsuario") != null);
        String loginURI = req.getContextPath() + "/login";
        // String registerjspURI = req.getContextPath() + "/register.jsp";
        String registerURI = req.getContextPath() + "/register";

        boolean loginRequest = req.getRequestURI().equals(loginURI);
        //boolean registerjspRequest = req.getRequestURI().equals(registerjspURI);
        boolean registerRequest = req.getRequestURI().equals(registerURI);
        boolean staticResource = req.getRequestURI().startsWith(req.getContextPath() + "/static/");

        if (loggedIn || loginRequest || registerRequest || staticResource /*|| registerjspRequest*/) {
            if (loggedIn && (loginRequest || registerRequest)) {
                ((HttpServletResponse) response).sendRedirect("productos");
            } else if (loggedIn && req.getRequestURI().equals("/aa2/")) {
                ((HttpServletResponse) response).sendRedirect("login");
            } else {
                chain.doFilter(request, response);
            }
        } else {
            ((HttpServletResponse) response).sendRedirect(loginURI);
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException { }

    @Override
    public void destroy() { }
}
