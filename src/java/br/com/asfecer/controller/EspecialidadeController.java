package br.com.asfecer.controller;

import br.com.asfecer.dao.EspecialidadeDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Especialidade;
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

@WebServlet(name = "EspecialidadeController", urlPatterns = {"/criaEspecialidade.html", "/listaEspecialidades.html", "/excluiEspecialidade.html", "/editaEspecialidade.html"})
public class EspecialidadeController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaEspecialidade.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaEspecialidade.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaEspecialidades.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiEspecialidade.html")){
            excluirGet(request, response);
            response.sendRedirect("listaEspecialidades.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaEspecialidade.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(EspecialidadeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaEspecialidade.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(EspecialidadeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroEspecialidade.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EspecialidadeDAO dao = new EspecialidadeDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idEspecialidade"));
        Especialidade especialidade = dao.findEspecialidade(id);
        
        request.setAttribute("especialidade", especialidade);
        request.getRequestDispatcher("WEB-INF/views/editaEspecialidade.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Especialidade> especialidades = new ArrayList<>();
        EspecialidadeDAO dao = new EspecialidadeDAO(utx, emf);
        especialidades = dao.findEspecialidadeEntities();
        
        request.setAttribute("especialidades", especialidades);
        request.getRequestDispatcher("WEB-INF/views/listaEspecialidade.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EspecialidadeDAO dao = new EspecialidadeDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idEspecialidade"));
        dao.destroy(id);
        
        response.sendRedirect("listaEspecialidades.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        String descricao = request.getParameter("descricao");

        Especialidade especialidade = new Especialidade(descricao);
        EspecialidadeDAO dao = new EspecialidadeDAO(utx, emf);
        
        dao.create(especialidade);
        
        response.sendRedirect("listaEspecialidades.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {

        int idEspecialidade = Integer.parseInt(request.getParameter("idEspecialidade"));
        String descricao = request.getParameter("descricao");

        Especialidade especialidade = new Especialidade(idEspecialidade, descricao);
        EspecialidadeDAO dao = new EspecialidadeDAO(utx, emf);
        
        dao.edit(especialidade);
        
        response.sendRedirect("listaEspecialidades.html");   
    }
}