package br.com.asfecer.controller;

import br.com.asfecer.dao.ItensreceituarioDAO;
import br.com.asfecer.dao.MedicamentoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Itensreceituario;
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

@WebServlet(name = "ItensReceituarioController", urlPatterns = {"/criaItensReceituario.html", "/listaItensReceituarios.html", "/excluiItensReceituario.html", "/editaItensReceituario.html"})
public class ItensReceituarioController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  
    public SimpleDateFormat formatHour = new SimpleDateFormat("hh:mm");  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaItensReceituario.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaItensReceituario.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaItensReceituarios.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiItensReceituario.html")){
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaItensReceituarios.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaItensReceituario.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaItensReceituario.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroItensReceituario.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ItensreceituarioDAO dao = new ItensreceituarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idItensReceituario"));
        Itensreceituario itensReceituario = dao.findItensreceituario(id);
        
        request.setAttribute("itensReceituario", itensReceituario);
        request.getRequestDispatcher("WEB-INF/views/editaItensReceituario.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Itensreceituario> itensReceituarios = new ArrayList<>();
        ItensreceituarioDAO dao = new ItensreceituarioDAO(utx, emf);
        itensReceituarios = dao.findItensreceituarioEntities();
        
        request.setAttribute("itensReceituarios", itensReceituarios);
        request.getRequestDispatcher("WEB-INF/views/listaItensReceituario.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        ItensreceituarioDAO dao = new ItensreceituarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idItensReceituario"));
        dao.destroy(id);
        
        response.sendRedirect("listaItensReceituarios.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        
        MedicamentoDAO mednto = new MedicamentoDAO(utx, emf);
        Itensreceituario itensReceituario = new Itensreceituario();
        
        itensReceituario.setOrdem(Integer.parseInt(request.getParameter("ordem")));
        itensReceituario.setQuantidade(Integer.parseInt(request.getParameter("quantidade")));
        itensReceituario.setPosologia(request.getParameter("posologia"));
        itensReceituario.setDose(request.getParameter("dose"));
        itensReceituario.setTipouso("on".equalsIgnoreCase(request.getParameter("tipoUso")));
        Medicamento medicamento = mednto.findMedicamento(Integer.parseInt(request.getParameter("medicamento")));
        itensReceituario.setMedicamento(medicamento);
        ItensreceituarioDAO dao = new ItensreceituarioDAO(utx, emf);
        
        dao.create(itensReceituario);
        
        response.sendRedirect("listaItensReceituarios.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
        
        MedicamentoDAO mednto = new MedicamentoDAO(utx, emf);
        ItensreceituarioDAO dao = new ItensreceituarioDAO(utx, emf);
        
        int idItensReceituario = Integer.parseInt(request.getParameter("idItensReceituario"));
        Itensreceituario itensReceituario = dao.findItensreceituario(idItensReceituario);
        itensReceituario.setOrdem(Integer.parseInt(request.getParameter("ordem")));
        itensReceituario.setQuantidade(Integer.parseInt(request.getParameter("quantidade")));
        itensReceituario.setPosologia(request.getParameter("posologia"));
        itensReceituario.setDose(request.getParameter("dose"));
        itensReceituario.setTipouso("on".equalsIgnoreCase(request.getParameter("tipoUso")));
        Medicamento medicamento = mednto.findMedicamento(Integer.parseInt(request.getParameter("medicamento")));
        itensReceituario.setMedicamento(medicamento);
        
        dao.edit(itensReceituario);
        
        response.sendRedirect("listaItensReceituarios.html");   
    }
}