package br.com.asfecer.filter;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class TratamentoErroFilter implements Filter {
    
private final static Logger logger = Logger.getLogger(TempoRequisicaoFilter.class.getName());
   
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        try {
             chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            HttpServletRequest req = (HttpServletRequest) request;
            String uri = req.getRequestURI(); 
            
            req.setAttribute("erro", e.getMessage());
            logger.severe("Erro ao acessar página '"+ uri + "': "+ e.getMessage());
            
            RequestDispatcher rd = request.getRequestDispatcher("/erro.jsp");
            rd.forward(request,response);
        }
        
    }

    @Override
    public void destroy() {
    }
   
    
}
