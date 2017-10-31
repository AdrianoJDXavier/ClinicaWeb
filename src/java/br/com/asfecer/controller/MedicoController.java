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
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            } catch (RollbackFailureException ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaMedico.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MedicoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Estados> estados = new ArrayList<>();
        EstadosDAO uf = new EstadosDAO(utx, emf);
        estados = uf.findEstadosEntities();
        
        request.setAttribute("estados", estados);
        request.getRequestDispatcher("WEB-INF/views/cadastroMedico.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Estados> estados = new ArrayList<>();
        EstadosDAO uf = new EstadosDAO(utx, emf);
        estados = uf.findEstadosEntities();
        MedicoDAO dao = new MedicoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idMedico"));
        Medico medico = dao.findMedico(id);
        
        request.setAttribute("estados", estados);
        request.setAttribute("medico", medico);
        request.getRequestDispatcher("WEB-INF/views/editaMedico.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Medico> medicos = new ArrayList<>();
        MedicoDAO dao = new MedicoDAO(utx, emf);
        medicos = dao.findMedicoEntities();
        
        request.setAttribute("medicos", medicos);
        request.getRequestDispatcher("WEB-INF/views/listaMedicos.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        MedicoDAO dao = new MedicoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idMedico"));
        dao.destroy(id);
        
        response.sendRedirect("listaMedicos.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        
        EspecialidadeDAO esp = new EspecialidadeDAO(utx, emf);
        Especialidade espec = new Especialidade();
        EstadosDAO uf = new EstadosDAO(utx, emf);
        Medico medico = new Medico();
        espec.setDescricao(request.getParameter("especialidade"));
        esp.create(espec);
        
        medico.setNomemedico(request.getParameter("nomeMedico"));
        medico.setCrm(Integer.parseInt(request.getParameter("crm")));
        medico.setEspecialidade(espec);
        medico.setUfCrm(uf.findEstados((request.getParameter("estados-brasil"))));
        
        MedicoDAO dao = new MedicoDAO(utx, emf);
        
        dao.create(medico);
        
        response.sendRedirect("listaMedicos.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
       
        EspecialidadeDAO esp = new EspecialidadeDAO(utx, emf);
        Especialidade espec = new Especialidade();
        EstadosDAO uf = new EstadosDAO(utx, emf);
        MedicoDAO dao = new MedicoDAO(utx, emf);
        espec.setDescricao(request.getParameter("especialidade"));
        esp.create(espec);
        
        int idMedico = Integer.parseInt(request.getParameter("idMedico"));
        Medico medico = dao.findMedico(idMedico);
        medico.setNomemedico(request.getParameter("nomeMedico"));
        medico.setCrm(Integer.parseInt(request.getParameter("crm")));
        medico.setEspecialidade(espec);
        medico.setUfCrm(uf.findEstados((request.getParameter("estados-brasil"))));
        
        dao.edit(medico);
        
        response.sendRedirect("listaMedicos.html");   
    }
}