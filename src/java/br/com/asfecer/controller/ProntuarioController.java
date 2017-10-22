package br.com.asfecer.controller;

import br.com.asfecer.dao.ConsultaDAO;
import br.com.asfecer.dao.ProntuarioDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Prontuario;
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

@WebServlet(name = "ProntuarioController", urlPatterns = {"/criaProntuario.html", "/listaProntuarios.html", "/excluiProntuario.html", "/editaProntuario.html"})
public class ProntuarioController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaProntuario.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaProntuario.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaProntuarios.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiProntuario.html")){
            excluirGet(request, response);
            response.sendRedirect("listaProntuarios.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaProntuario.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ProntuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaProntuario.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ProntuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroProntuario.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProntuarioDAO dao = new ProntuarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idProntuario"));
        Prontuario prontuario = dao.findProntuario(id);
        
        request.setAttribute("prontuario", prontuario);
        request.getRequestDispatcher("WEB-INF/views/editaProntuario.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Prontuario> prontuarios = new ArrayList<>();
        ProntuarioDAO dao = new ProntuarioDAO(utx, emf);
        prontuarios = dao.findProntuarioEntities();
        
        request.setAttribute("prontuarios", prontuarios);
        request.getRequestDispatcher("WEB-INF/views/listaProntuario.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        ProntuarioDAO dao = new ProntuarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idProntuario"));
        dao.destroy(id);
        
        response.sendRedirect("listaProntuarios.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
               
        String queixaPrincipal = request.getParameter("queixaPrincipal");
        String anamnese = request.getParameter("anamnese");
        String examesFisicos = request.getParameter("examesFisicos");
        String examesComplementares = request.getParameter("examesComplementares");
        String hipotesesDiagnosticas = request.getParameter("hipotesesDiagnosticas");
        String diagnosticoDefinitivo = request.getParameter("diagnosticoDefinitivo");
        String tratamento = request.getParameter("tratamento");
        String evolucao = request.getParameter("evolucao");
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        
        Prontuario prontuario = new Prontuario(queixaPrincipal, anamnese, examesFisicos, examesComplementares, hipotesesDiagnosticas, diagnosticoDefinitivo, tratamento, evolucao, consulta);
        ProntuarioDAO dao = new ProntuarioDAO(utx, emf);
        
        dao.create(prontuario);
        
        response.sendRedirect("listaProntuarios.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
               
        int idProntuario = Integer.parseInt(request.getParameter("idProntuario"));
        String queixaPrincipal = request.getParameter("queixaPrincipal");
        String anamnese = request.getParameter("anamnese");
        String examesFisicos = request.getParameter("examesFisicos");
        String examesComplementares = request.getParameter("examesComplementares");
        String hipotesesDiagnosticas = request.getParameter("hipotesesDiagnosticas");
        String diagnosticoDefinitivo = request.getParameter("diagnosticoDefinitivo");
        String tratamento = request.getParameter("tratamento");
        String evolucao = request.getParameter("evolucao");
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        
        Prontuario prontuario = new Prontuario(idProntuario, queixaPrincipal, anamnese, examesFisicos, examesComplementares, hipotesesDiagnosticas, diagnosticoDefinitivo, tratamento, evolucao, consulta);
        ProntuarioDAO dao = new ProntuarioDAO(utx, emf);
        
        dao.edit(prontuario);
        
        response.sendRedirect("listaProntuarios.html");   
    }
}