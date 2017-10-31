package br.com.asfecer.controller;

import br.com.asfecer.dao.ConsultaDAO;
import br.com.asfecer.dao.ExameDAO;
import br.com.asfecer.dao.PedidoexameDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Exame;
import br.com.asfecer.model.Pedidoexame;
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

    SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().contains("criaPedidoExame.html")) {
            criarGet(request, response);
        } else if (request.getServletPath().contains("editaPedidoExame.html")) {
            editarGet(request, response);
        } else if (request.getServletPath().contains("listaPedidoExames.html")) {
            listarGet(request, response);
        } else if (request.getServletPath().contains("excluiPedidoExame.html")) {
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/editaPedidoExame.html")) {
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (request.getServletPath().contains("/criaPedidoExame.html")) {
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PedidoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Consulta> consultas = new ArrayList<>();
        List<Exame> exames = new ArrayList<>();
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ExameDAO ex = new ExameDAO(utx, emf);
        consultas = cons.findConsultaEntities();
        exames = ex.findExameEntities();

        request.setAttribute("exames", exames);
        request.setAttribute("consultas", consultas);
        request.getRequestDispatcher("WEB-INF/views/cadastroPedidoExame.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PedidoexameDAO dao = new PedidoexameDAO(utx, emf);
        List<Consulta> consultas = new ArrayList<>();
        List<Exame> exames = new ArrayList<>();
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ExameDAO ex = new ExameDAO(utx, emf);
        consultas = cons.findConsultaEntities();
        exames = ex.findExameEntities();

        int id = Integer.parseInt(request.getParameter("idPedidoExame"));
        Pedidoexame pedidoExame = dao.findPedidoexame(id);

        request.setAttribute("exames", exames);
        request.setAttribute("consultas", consultas);
        request.setAttribute("pedidoExame", pedidoExame);
        request.getRequestDispatcher("WEB-INF/views/editaPedidoExame.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Pedidoexame> pedidoExames = new ArrayList<>();
        PedidoexameDAO dao = new PedidoexameDAO(utx, emf);
        pedidoExames = dao.findPedidoexameEntities();

        request.setAttribute("pedidoExames", pedidoExames);
        request.getRequestDispatcher("WEB-INF/views/listaPedidoExame.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        PedidoexameDAO dao = new PedidoexameDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPedidoExame"));
        dao.destroy(id);

        response.sendRedirect("listaPedidoExames.html");
    }

    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {

        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ExameDAO exm = new ExameDAO(utx, emf);
        Pedidoexame pedidoExame = new Pedidoexame();
        Date data = formatDate.parse(request.getParameter("data"));
        pedidoExame.setData(data);
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        pedidoExame.setConsulta(consulta);
        Exame exame = exm.findExame(Integer.parseInt(request.getParameter("exame")));
        pedidoExame.setExame(exame);

        PedidoexameDAO dao = new PedidoexameDAO(utx, emf);

        dao.create(pedidoExame);

        response.sendRedirect("listaPedidoExames.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, RollbackFailureException, Exception {

        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ExameDAO exm = new ExameDAO(utx, emf);
        PedidoexameDAO dao = new PedidoexameDAO(utx, emf);

        int idPedidoExame = Integer.parseInt(request.getParameter("idPedidoExame"));
        Pedidoexame pedidoExame = dao.findPedidoexame(idPedidoExame);
        pedidoExame.setData(formatDate.parse(request.getParameter("data")));
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        pedidoExame.setConsulta(consulta);
        Exame exame = exm.findExame(Integer.parseInt(request.getParameter("exame")));
        pedidoExame.setExame(exame);

        dao.edit(pedidoExame);

        response.sendRedirect("listaPedidoExames.html");
    }
}
