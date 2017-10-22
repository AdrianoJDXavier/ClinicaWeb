package br.com.asfecer.controller;

import br.com.asfecer.dao.AtestadoDAO;
import br.com.asfecer.dao.ConsultaDAO;
import br.com.asfecer.dao.TipoAtestadoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Atestado;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.TipoAtestado;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

@WebServlet(name = "AtestadoController", urlPatterns = {"/criaAtestado.html", "/listaAtestados.html", "/excluiAtestado.html", "/editaAtestado.html"})
public class AtestadoController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaAtestado.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaAtestado.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaAtestados.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiAtestado.html")){
            excluirGet(request, response);
            response.sendRedirect("listaAtestados.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaAtestado.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(AtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaAtestado.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(AtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroAtestado.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AtestadoDAO dao = new AtestadoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idAtestado"));
        Atestado atestado = dao.findAtestado(id);
        
        request.setAttribute("atestado", atestado);
        request.getRequestDispatcher("WEB-INF/views/editaAtestado.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Atestado> atestados = new ArrayList<>();
        AtestadoDAO dao = new AtestadoDAO(utx, emf);
        atestados = dao.findAtestadoEntities();
        
        request.setAttribute("atestados", atestados);
        request.getRequestDispatcher("WEB-INF/views/listaAtestado.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        AtestadoDAO dao = new AtestadoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idAtestado"));
        dao.destroy(id);
        
        response.sendRedirect("listaAtestados.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        TipoAtestadoDAO tpAtestado = new TipoAtestadoDAO(utx, emf);
        
        Date dataAtestado = formatDate.parse(request.getParameter("dataAtestado"));
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        TipoAtestado tipoAtestado = tpAtestado.findTipoAtestado(Integer.parseInt(request.getParameter("tipoAtestado")));
        
        Atestado atestado = new Atestado(dataAtestado, consulta, tipoAtestado);
        AtestadoDAO dao = new AtestadoDAO(utx, emf);
        
        dao.create(atestado);
        
        response.sendRedirect("listaAtestados.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        TipoAtestadoDAO tpAtestado = new TipoAtestadoDAO(utx, emf);
        
        int idAtestado = Integer.parseInt(request.getParameter("idAtestado"));
        Date dataAtestado = formatDate.parse(request.getParameter("dataAtestado"));
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        TipoAtestado tipoAtestado = tpAtestado.findTipoAtestado(Integer.parseInt(request.getParameter("tipoAtestado")));
        
        Atestado atestado = new Atestado(idAtestado, dataAtestado, consulta, tipoAtestado);
        AtestadoDAO dao = new AtestadoDAO(utx, emf);
        
        dao.edit(atestado);
        
        response.sendRedirect("listaAtestados.html");   
    }
}