package br.com.asfecer.controller;

import br.com.asfecer.dao.ConvenioDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Convenio;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
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

@WebServlet(name = "ConvenioController", urlPatterns = {"/criaConvenio.html","/listaConvenio.html", "/editaConvenio.html", "/excluiConvenio.html", "/listaConvenio.html"})
public class ConvenioController extends HttpServlet {

        @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaConvenio.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaConvenio.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaConvenio.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiConvenio.html")){
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaConvenio.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaConvenio.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ConvenioController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
    
    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/criaConvenio.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        ConvenioDAO dao = new ConvenioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idConvenio"));
        Convenio convenio = dao.findConvenio(id);
        
        request.setAttribute("convenio", convenio);
        request.getRequestDispatcher("WEB-INF/views/editaConvenio.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("listaConvenio.html");
        }
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Convenio> convenios = new ArrayList<>();
        ConvenioDAO dao = new ConvenioDAO(utx, emf);
        convenios = dao.findConvenioEntities();
        
        request.setAttribute("convenios", convenios);
        request.getRequestDispatcher("WEB-INF/views/listaConvenio.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        ConvenioDAO dao = new ConvenioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idConvenio"));
        dao.destroy(id);
        
        response.sendRedirect("listaConvenio.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        
        ConvenioDAO dao = new ConvenioDAO(utx, emf);
        Convenio convenio = new Convenio();
        
        convenio.setEmpresaConvenio(request.getParameter("convenio"));
        convenio.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        convenio.setTelefone(request.getParameter("telefone"));
        convenio.setTipoConvenio(request.getParameter("tipo_convenio"));
        convenio.setObs(request.getParameter("obs"));
        
        dao.create(convenio);
        
        response.sendRedirect("listaConvenio.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
        
        ConvenioDAO dao = new ConvenioDAO(utx, emf);
        
        int idConvenio = Integer.parseInt(request.getParameter("idConvenio"));
        Convenio convenio = dao.findConvenio(idConvenio);
        convenio.setEmpresaConvenio(request.getParameter("convenio"));
        convenio.setStatus(Boolean.parseBoolean(request.getParameter("status")));
        convenio.setTelefone(request.getParameter("telefone"));
        convenio.setTipoConvenio(request.getParameter("tipo_convenio"));
        convenio.setObs(request.getParameter("obs"));
        
        dao.edit(convenio);
        
        response.sendRedirect("listaConvenio.html");   
    }
}