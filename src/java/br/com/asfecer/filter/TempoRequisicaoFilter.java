package br.com.asfecer.filter;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class TempoRequisicaoFilter implements Filter {
    
private final static Logger logger = Logger.getLogger(TempoRequisicaoFilter.class.getName());
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            
            long tempoInicial = System.currentTimeMillis();
        
            chain.doFilter(request, response);

            long tempoFinal = System.currentTimeMillis();
            String uri = req.getRequestURI(); 
            
            logger.info("Tempo de resposta "+uri+": "+ (tempoFinal - tempoInicial) + "ms");
            
    }

    @Override
    public void destroy() {}
       
}
