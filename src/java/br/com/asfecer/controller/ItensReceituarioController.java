package br.com.asfecer.controller;

import br.com.asfecer.dao.ItensReceituarioDAO;
import br.com.asfecer.dao.MedicamentoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.ItensReceituario;
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
            excluirGet(request, response);
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
            }
        }

        if(request.getServletPath().contains("/criaItensReceituario.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ItensReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroItensReceituario.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ItensReceituarioDAO dao = new ItensReceituarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idItensReceituario"));
        ItensReceituario itensReceituario = dao.findItensReceituario(id);
        
        request.setAttribute("itensReceituario", itensReceituario);
        request.getRequestDispatcher("WEB-INF/views/editaItensReceituario.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ItensReceituario> itensReceituarios = new ArrayList<>();
        ItensReceituarioDAO dao = new ItensReceituarioDAO(utx, emf);
        itensReceituarios = dao.findItensReceituarioEntities();
        
        request.setAttribute("itensReceituarios", itensReceituarios);
        request.getRequestDispatcher("WEB-INF/views/listaItensReceituario.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        ItensReceituarioDAO dao = new ItensReceituarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idItensReceituario"));
        dao.destroy(id);
        
        response.sendRedirect("listaItensReceituarios.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        MedicamentoDAO mednto = new MedicamentoDAO(utx, emf);
        
        int ordem = Integer.parseInt(request.getParameter("ordem"));
        int quantidade = Integer.parseInt(request.getParameter("quantidade"));
        String posologia = request.getParameter("posologia");
        String dose = request.getParameter("dose");
        boolean tipoUso = "on".equalsIgnoreCase(request.getParameter("tipoUso"));
        Medicamento medicamento = mednto.findMedicamento(Integer.parseInt(request.getParameter("medicamento")));
        
        ItensReceituario itensReceituario = new ItensReceituario(ordem, quantidade, posologia, dose, tipoUso, medicamento);
        ItensReceituarioDAO dao = new ItensReceituarioDAO(utx, emf);
        
        dao.create(itensReceituario);
        
        response.sendRedirect("listaItensReceituarios.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        MedicamentoDAO mednto = new MedicamentoDAO(utx, emf);
        
        int idItensReceituario = Integer.parseInt(request.getParameter("idItensReceituario"));
        int ordem = Integer.parseInt(request.getParameter("ordem"));
        int quantidade = Integer.parseInt(request.getParameter("quantidade"));
        String posologia = request.getParameter("posologia");
        String dose = request.getParameter("dose");
        boolean tipoUso = "on".equalsIgnoreCase(request.getParameter("tipoUso"));
        Medicamento medicamento = mednto.findMedicamento(Integer.parseInt(request.getParameter("medicamento")));
        
        ItensReceituario itensReceituario = new ItensReceituario(idItensReceituario, ordem, quantidade, posologia, dose, tipoUso, medicamento);
        ItensReceituarioDAO dao = new ItensReceituarioDAO(utx, emf);
        
        dao.edit(itensReceituario);
        
        response.sendRedirect("listaItensReceituarios.html");   
    }
}