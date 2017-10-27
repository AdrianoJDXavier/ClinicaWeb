package br.com.asfecer.controller;

import br.com.asfecer.dao.HorarioDAO;
import br.com.asfecer.dao.MedicoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Horario;
import br.com.asfecer.model.Medico;
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

@WebServlet(name = "HorarioController", urlPatterns = {"/criaHorario.html", "/listaHorarios.html", "/excluiHorario.html", "/editaHorario.html"})
public class HorarioController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaHorario.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaHorario.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaHorarios.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiHorario.html")){
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaHorarios.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaHorario.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaHorario.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(HorarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroHorario.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HorarioDAO dao = new HorarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idHorario"));
        Horario horario = dao.findHorario(id);
        
        request.setAttribute("horario", horario);
        request.getRequestDispatcher("WEB-INF/views/editaHorario.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Horario> horarios = new ArrayList<>();
        HorarioDAO dao = new HorarioDAO(utx, emf);
        horarios = dao.findHorarioEntities();
        
        request.setAttribute("horarios", horarios);
        request.getRequestDispatcher("WEB-INF/views/listaHorario.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        HorarioDAO dao = new HorarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idHorario"));
        dao.destroy(id);
        
        response.sendRedirect("listaHorarios.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        
        MedicoDAO med = new MedicoDAO(utx, emf);
        Horario horario = new Horario();
        
        horario.setDiasemana(request.getParameter("diaSemana"));
        horario.setInicio(formatDate.parse(request.getParameter("inicio")));
        horario.setFim(formatDate.parse(request.getParameter("fim")));
        Medico medico = med.findMedico(Integer.parseInt(request.getParameter("medico")));
        horario.setMedico(medico);
        
        HorarioDAO dao = new HorarioDAO(utx, emf);
        
        dao.create(horario);
        
        response.sendRedirect("listaHorarios.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {

        MedicoDAO med = new MedicoDAO(utx, emf);
        HorarioDAO dao = new HorarioDAO(utx, emf);
        
        int idHorario = Integer.parseInt(request.getParameter("idHorario"));
        Horario horario = dao.findHorario(idHorario);
        horario.setDiasemana(request.getParameter("diaSemana"));
        horario.setInicio(formatDate.parse(request.getParameter("inicio")));
        horario.setFim(formatDate.parse(request.getParameter("fim")));
        Medico medico = med.findMedico(Integer.parseInt(request.getParameter("medico")));
        horario.setMedico(medico);
        
        dao.edit(horario);
        
        response.sendRedirect("listaHorarios.html");   
    }
}