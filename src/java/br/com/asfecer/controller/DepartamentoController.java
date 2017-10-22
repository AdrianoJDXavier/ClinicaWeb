package br.com.asfecer.controller;

import br.com.asfecer.dao.DepartamentoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Departamento;
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

@WebServlet(name = "DepartamentoController", urlPatterns = {"/criaDepartamento.html", "/listaDepartamentos.html", "/excluiDepartamento.html", "/editaDepartamento.html"})
public class DepartamentoController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaDepartamento.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaDepartamento.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaDepartamentos.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiDepartamento.html")){
            excluirGet(request, response);
            response.sendRedirect("listaDepartamentos.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaDepartamento.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(DepartamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaDepartamento.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(DepartamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroDepartamento.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DepartamentoDAO dao = new DepartamentoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idDepartamento"));
        Departamento departamento = dao.findDepartamento(id);
        
        request.setAttribute("departamento", departamento);
        request.getRequestDispatcher("WEB-INF/views/editaDepartamento.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Departamento> departamentos = new ArrayList<>();
        DepartamentoDAO dao = new DepartamentoDAO(utx, emf);
        departamentos = dao.findDepartamentoEntities();
        
        request.setAttribute("departamentos", departamentos);
        request.getRequestDispatcher("WEB-INF/views/listaDepartamento.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        DepartamentoDAO dao = new DepartamentoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idDepartamento"));
        dao.destroy(id);
        
        response.sendRedirect("listaDepartamentos.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        String depto = request.getParameter("depto");
        
        Departamento departamento = new Departamento(depto);
        DepartamentoDAO dao = new DepartamentoDAO(utx, emf);
        
        dao.create(departamento);
        
        response.sendRedirect("listaDepartamentos.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        int idDepto = Integer.parseInt(request.getParameter("idDepto"));
        String depto = request.getParameter("depto");
        
        Departamento departamento = new Departamento(idDepto, depto);
        DepartamentoDAO dao = new DepartamentoDAO(utx, emf);
        
        dao.edit(departamento);
        
        response.sendRedirect("listaDepartamentos.html");   
    }
}