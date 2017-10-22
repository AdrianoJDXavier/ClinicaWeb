package br.com.asfecer.controller;

import br.com.asfecer.dao.EspecialidadeDAO;
import br.com.asfecer.dao.EstadosDAO;
import br.com.asfecer.dao.MedicoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Especialidade;
import br.com.asfecer.model.Estados;
import br.com.asfecer.model.Medico;
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

@WebServlet(name = "MedicoController", urlPatterns = {"/criaMedico.html", "/listaMedicos.html", "/excluiMedico.html", "/editaMedico.html"})
public class MedicoController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaMedico.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaMedico.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaMedicos.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiMedico.html")){
            excluirGet(request, response);
            response.sendRedirect("listaMedicos.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaMedico.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaMedico.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroMedico.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MedicoDAO dao = new MedicoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idMedico"));
        Medico medico = dao.findMedico(id);
        
        request.setAttribute("medico", medico);
        request.getRequestDispatcher("WEB-INF/views/editaMedico.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Medico> medicos = new ArrayList<>();
        MedicoDAO dao = new MedicoDAO(utx, emf);
        medicos = dao.findMedicoEntities();
        
        request.setAttribute("medicos", medicos);
        request.getRequestDispatcher("WEB-INF/views/listaMedico.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        MedicoDAO dao = new MedicoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idMedico"));
        dao.destroy(id);
        
        response.sendRedirect("listaMedicos.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        EspecialidadeDAO esp = new EspecialidadeDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        
        String nomeMedico = request.getParameter("nomeMedico");
        int crm = Integer.parseInt(request.getParameter("crm"));
        Especialidade especialidade = esp.findEspecialidade(Integer.parseInt(request.getParameter("especialidade")));
        Estados ufCrm = uf.findEstados(request.getParameter("ufCrm"));
                
        Medico medico = new Medico(nomeMedico, crm, especialidade, ufCrm);
        MedicoDAO dao = new MedicoDAO(utx, emf);
        
        dao.create(medico);
        
        response.sendRedirect("listaMedicos.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
       
        EspecialidadeDAO esp = new EspecialidadeDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        
        int idMedico = Integer.parseInt(request.getParameter("idMedico"));
        String nomeMedico = request.getParameter("nomeMedico");
        int crm = Integer.parseInt(request.getParameter("crm"));
        Especialidade especialidade = esp.findEspecialidade(Integer.parseInt(request.getParameter("especialidade")));
        Estados ufCrm = uf.findEstados(request.getParameter("ufCrm"));
                
        Medico medico = new Medico(idMedico, nomeMedico, crm, especialidade, ufCrm);
        MedicoDAO dao = new MedicoDAO(utx, emf);
        
        dao.edit(medico);
        
        response.sendRedirect("listaMedicos.html");   
    }
}