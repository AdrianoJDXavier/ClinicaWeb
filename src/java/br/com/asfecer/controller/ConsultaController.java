package br.com.asfecer.controller;

import br.com.asfecer.dao.AgendaDAO;
import br.com.asfecer.dao.ConsultaDAO;
import br.com.asfecer.dao.MedicoDAO;
import br.com.asfecer.dao.PacienteDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Agenda;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Medico;
import br.com.asfecer.model.Paciente;
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

@WebServlet(name = "ConsultaController", urlPatterns = {"/criaConsulta.html", "/listaConsultas.html", "/excluiConsulta.html", "/editaConsulta.html"})
public class ConsultaController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  
    public SimpleDateFormat formatHour = new SimpleDateFormat("hh:mm");  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaConsulta.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaConsulta.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaConsultas.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiConsulta.html")){
            excluirGet(request, response);
            response.sendRedirect("listaConsultas.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaConsulta.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ConsultaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaConsulta.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ConsultaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroConsulta.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConsultaDAO dao = new ConsultaDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idConsulta"));
        Consulta consulta = dao.findConsulta(id);
        
        request.setAttribute("consulta", consulta);
        request.getRequestDispatcher("WEB-INF/views/editaConsulta.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Consulta> consultas = new ArrayList<>();
        ConsultaDAO dao = new ConsultaDAO(utx, emf);
        consultas = dao.findConsultaEntities();
        
        request.setAttribute("consultas", consultas);
        request.getRequestDispatcher("WEB-INF/views/listaConsulta.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        ConsultaDAO dao = new ConsultaDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idConsulta"));
        dao.destroy(id);
        
        response.sendRedirect("listaConsultas.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        AgendaDAO agd = new AgendaDAO(utx, emf);
        MedicoDAO med = new MedicoDAO(utx, emf);
        PacienteDAO pac = new PacienteDAO(utx, emf);
        
        Date dataConsulta = formatDate.parse(request.getParameter("dataConsulta"));
        Date horaConsulta = formatHour.parse(request.getParameter("horaConsulta"));
        Agenda agenda = agd.findAgenda(Integer.parseInt(request.getParameter("agenda")));
        Medico medico = med.findMedico(Integer.parseInt(request.getParameter("medico")));
        Paciente paciente = pac.findPaciente(Integer.parseInt(request.getParameter("paciente")));
        
        Consulta consulta = new Consulta(dataConsulta, horaConsulta, agenda, medico, paciente);
        ConsultaDAO dao = new ConsultaDAO(utx, emf);
        
        dao.create(consulta);
        
        response.sendRedirect("listaConsultas.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        AgendaDAO agd = new AgendaDAO(utx, emf);
        MedicoDAO med = new MedicoDAO(utx, emf);
        PacienteDAO pac = new PacienteDAO(utx, emf);
        
        int idConsulta = Integer.parseInt(request.getParameter("idConsulta"));
        Date dataConsulta = formatDate.parse(request.getParameter("dataConsulta"));
        Date horaConsulta = formatHour.parse(request.getParameter("horaConsulta"));
        Agenda agenda = agd.findAgenda(Integer.parseInt(request.getParameter("agenda")));
        Medico medico = med.findMedico(Integer.parseInt(request.getParameter("medico")));
        Paciente paciente = pac.findPaciente(Integer.parseInt(request.getParameter("paciente")));
        
        Consulta consulta = new Consulta(idConsulta, dataConsulta, horaConsulta, agenda, medico, paciente);
        ConsultaDAO dao = new ConsultaDAO(utx, emf);
        
        dao.edit(consulta);
        
        response.sendRedirect("listaConsultas.html");   
    }
}