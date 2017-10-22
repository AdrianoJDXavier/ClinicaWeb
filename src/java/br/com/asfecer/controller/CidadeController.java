package br.com.asfecer.controller;

import br.com.asfecer.dao.CidadeDAO;
import br.com.asfecer.dao.EstadosDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Estados;
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

@WebServlet(name = "CidadeController", urlPatterns = {"/criaCidade.html", "/listaCidades.html", "/excluiCidade.html", "/editaCidade.html"})
public class CidadeController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaCidade.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaCidade.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaCidades.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiCidade.html")){
            excluirGet(request, response);
            response.sendRedirect("listaCidades.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaCidade.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(CidadeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaCidade.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(CidadeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroCidade.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CidadeDAO dao = new CidadeDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idCidade"));
        Cidade cidade = dao.findCidade(id);
        
        request.setAttribute("cidade", cidade);
        request.getRequestDispatcher("WEB-INF/views/editaCidade.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cidade> cidades = new ArrayList<>();
        CidadeDAO dao = new CidadeDAO(utx, emf);
        cidades = dao.findCidadeEntities();
        
        request.setAttribute("cidades", cidades);
        request.getRequestDispatcher("WEB-INF/views/listaCidade.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        CidadeDAO dao = new CidadeDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idCidade"));
        dao.destroy(id);
        
        response.sendRedirect("listaCidades.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        CidadeDAO carg = new CidadeDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        
        
        String nomeCidade = request.getParameter("cidade");
        Estados estado = uf.findEstados(request.getParameter("estado")) ;
        
        Cidade cidade = new Cidade(nomeCidade, estado);
        CidadeDAO dao = new CidadeDAO(utx, emf);
        
        dao.create(cidade);
        
        response.sendRedirect("listaCidades.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        CidadeDAO carg = new CidadeDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        
        int idCidade = Integer.parseInt(request.getParameter("idCidade"));
        String nomeCidade = request.getParameter("cidade");
        Estados estado = uf.findEstados(request.getParameter("estado")) ;
        
        Cidade cidade = new Cidade(idCidade, nomeCidade, estado);
        CidadeDAO dao = new CidadeDAO(utx, emf);
        
        dao.edit(cidade);
        
        response.sendRedirect("listaCidades.html");   
    }
}