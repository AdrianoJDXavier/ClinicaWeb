package br.com.asfecer.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AutenticacaoFilter implements Filter {
    
@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        /*HttpServletRequest req = (HttpServletRequest) request;
        if (!"Asfecer/login".equals(req.getRequestURI()) 
                && req.getSession(true).getAttribute("usuario") == null) {
            request.setAttribute("erro", "Favor realizar login para continuar.");
            RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request,response);
        }*/
        
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
