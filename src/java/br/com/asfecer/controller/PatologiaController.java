package br.com.asfecer.controller;

import br.com.asfecer.dao.PatologiaDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Patologia;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

@WebServlet(name = "PatologiaController", urlPatterns = {"/criaPatologia.html", "/listaPatologias.html", "/excluiPatologia.html", "/editaPatologia.html"})
public class PatologiaController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  
    public SimpleDateFormat formatHour = new SimpleDateFormat("hh:mm");  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaPatologia.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaPatologia.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaPatologias.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiPatologia.html")){
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(PatologiaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(PatologiaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PatologiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaPatologias.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaPatologia.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PatologiaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PatologiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaPatologia.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PatologiaController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PatologiaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroPatologia.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PatologiaDAO dao = new PatologiaDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPatologia"));
        Patologia patologia = dao.findPatologia(id);
        
        request.setAttribute("patologia", patologia);
        request.getRequestDispatcher("WEB-INF/views/editaPatologia.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Patologia> patologias = new ArrayList<>();
        PatologiaDAO dao = new PatologiaDAO(utx, emf);
        patologias = dao.findPatologiaEntities();
        
        request.setAttribute("patologias", patologias);
        request.getRequestDispatcher("WEB-INF/views/listaPatologia.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        PatologiaDAO dao = new PatologiaDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPatologia"));
        dao.destroy(id);
        
        response.sendRedirect("listaPatologias.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        Patologia patologia = new Patologia();
        
        patologia.setPatologia(request.getParameter("patologia"));
                
        PatologiaDAO dao = new PatologiaDAO(utx, emf);
        
        dao.create(patologia);
        
        response.sendRedirect("listaPatologias.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, RollbackFailureException, Exception {
       
        PatologiaDAO dao = new PatologiaDAO(utx, emf);
        
        int idPatologia = Integer.parseInt(request.getParameter("idPatologia"));
        Patologia patologia = dao.findPatologia(idPatologia);
        patologia.setPatologia(request.getParameter("patologia"));
        
        dao.edit(patologia);
        
        response.sendRedirect("listaPatologias.html");   
    }
}