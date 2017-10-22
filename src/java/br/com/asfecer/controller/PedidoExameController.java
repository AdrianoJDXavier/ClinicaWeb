package br.com.asfecer.controller;

import br.com.asfecer.dao.ConsultaDAO;
import br.com.asfecer.dao.ExameDAO;
import br.com.asfecer.dao.PedidoExameDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Exame;
import br.com.asfecer.model.PedidoExame;
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

@WebServlet(name = "PedidoExameController", urlPatterns = {"/criaPedidoExame.html", "/listaPedidoExames.html", "/excluiPedidoExame.html", "/editaPedidoExame.html"})
public class PedidoExameController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaPedidoExame.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaPedidoExame.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaPedidoExames.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiPedidoExame.html")){
            excluirGet(request, response);
            response.sendRedirect("listaPedidoExames.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaPedidoExame.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaPedidoExame.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroPedidoExame.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PedidoExameDAO dao = new PedidoExameDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPedidoExame"));
        PedidoExame pedidoExame = dao.findPedidoExame(id);
        
        request.setAttribute("pedidoExame", pedidoExame);
        request.getRequestDispatcher("WEB-INF/views/editaPedidoExame.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<PedidoExame> pedidoExames = new ArrayList<>();
        PedidoExameDAO dao = new PedidoExameDAO(utx, emf);
        pedidoExames = dao.findPedidoExameEntities();
        
        request.setAttribute("pedidoExames", pedidoExames);
        request.getRequestDispatcher("WEB-INF/views/listaPedidoExame.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        PedidoExameDAO dao = new PedidoExameDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPedidoExame"));
        dao.destroy(id);
        
        response.sendRedirect("listaPedidoExames.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ExameDAO exm = new ExameDAO(utx, emf);
        
        Date data = formatDate.parse(request.getParameter("data"));
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        Exame exame = exm.findExame(Integer.parseInt(request.getParameter("exame")));
        
        PedidoExame pedidoExame = new PedidoExame(data, consulta, exame);
        PedidoExameDAO dao = new PedidoExameDAO(utx, emf);
        
        dao.create(pedidoExame);
        
        response.sendRedirect("listaPedidoExames.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
       
        
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ExameDAO exm = new ExameDAO(utx, emf);
        
        int idPedidoExame = Integer.parseInt(request.getParameter("idPedidoExame"));
        Date data = formatDate.parse(request.getParameter("data"));
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        Exame exame = exm.findExame(Integer.parseInt(request.getParameter("exame")));
        
        PedidoExame pedidoExame = new PedidoExame(idPedidoExame, data, consulta, exame);
        PedidoExameDAO dao = new PedidoExameDAO(utx, emf);
        
        dao.edit(pedidoExame);
        
        response.sendRedirect("listaPedidoExames.html");   
    }
}