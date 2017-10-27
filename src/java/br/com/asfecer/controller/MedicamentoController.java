package br.com.asfecer.controller;

import br.com.asfecer.dao.MedicamentoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Medicamento;
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

@WebServlet(name = "MedicamentoController", urlPatterns = {"/criaMedicamento.html", "/listaMedicamentos.html", "/excluiMedicamento.html", "/editaMedicamento.html"})
public class MedicamentoController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  
    public SimpleDateFormat formatHour = new SimpleDateFormat("hh:mm");  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaMedicamento.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaMedicamento.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaMedicamentos.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiMedicamento.html")){
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaMedicamentos.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaMedicamento.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaMedicamento.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(MedicamentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroMedicamento.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MedicamentoDAO dao = new MedicamentoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idMedicamento"));
        Medicamento medicamento = dao.findMedicamento(id);
        
        request.setAttribute("medicamento", medicamento);
        request.getRequestDispatcher("WEB-INF/views/editaMedicamento.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Medicamento> medicamentos = new ArrayList<>();
        MedicamentoDAO dao = new MedicamentoDAO(utx, emf);
        medicamentos = dao.findMedicamentoEntities();
        
        request.setAttribute("medicamentos", medicamentos);
        request.getRequestDispatcher("WEB-INF/views/listaMedicamento.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        MedicamentoDAO dao = new MedicamentoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idMedicamento"));
        dao.destroy(id);
        
        response.sendRedirect("listaMedicamentos.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        
        Medicamento medicamento = new Medicamento();
        medicamento.setPrincipioativo(request.getParameter("principioAtivo"));
        medicamento.setMedicamento(request.getParameter("medicamento"));
        medicamento.setLaboratorio(request.getParameter("laboratorio"));
        medicamento.setApresentacao(request.getParameter("apresentacao"));
        
        MedicamentoDAO dao = new MedicamentoDAO(utx, emf);
        
        dao.create(medicamento);
        
        response.sendRedirect("listaMedicamentos.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
        MedicamentoDAO dao = new MedicamentoDAO(utx, emf);
        
        int idMedicamento = Integer.parseInt(request.getParameter("idMedicamento"));
        Medicamento medicamento = dao.findMedicamento(idMedicamento);
        medicamento.setPrincipioativo(request.getParameter("principioAtivo"));
        medicamento.setMedicamento(request.getParameter("medicamento"));
        medicamento.setLaboratorio(request.getParameter("laboratorio"));
        medicamento.setApresentacao(request.getParameter("apresentacao"));
        
        dao.edit(medicamento);
        
        response.sendRedirect("listaMedicamentos.html");   
    }
}