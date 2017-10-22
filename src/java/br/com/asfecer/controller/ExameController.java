package br.com.asfecer.controller;

import br.com.asfecer.dao.ExameDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Exame;
import java.io.IOException;
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

@WebServlet(name = "ExameController", urlPatterns = {"/criaExame.html", "/listaExames.html", "/excluiExame.html", "/editaExame.html"})
public class ExameController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaExame.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaExame.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaExames.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiExame.html")){
            excluirGet(request, response);
            response.sendRedirect("listaExames.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaExame.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaExame.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroExame.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ExameDAO dao = new ExameDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idExame"));
        Exame exame = dao.findExame(id);
        
        request.setAttribute("exame", exame);
        request.getRequestDispatcher("WEB-INF/views/editaExame.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Exame> exames = new ArrayList<>();
        ExameDAO dao = new ExameDAO(utx, emf);
        exames = dao.findExameEntities();
        
        request.setAttribute("exames", exames);
        request.getRequestDispatcher("WEB-INF/views/listaExame.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        ExameDAO dao = new ExameDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idExame"));
        dao.destroy(id);
        
        response.sendRedirect("listaExames.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        String nomeExame = request.getParameter("exame");

        Exame exame = new Exame(nomeExame);
        ExameDAO dao = new ExameDAO(utx, emf);
        
        dao.create(exame);
        
        response.sendRedirect("listaExames.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {

        int idExame = Integer.parseInt(request.getParameter("idExame"));
        String nomeExame = request.getParameter("exame");

        Exame exame = new Exame(idExame, nomeExame);
        ExameDAO dao = new ExameDAO(utx, emf);
        
        dao.edit(exame);
        
        response.sendRedirect("listaExames.html");   
    }
}