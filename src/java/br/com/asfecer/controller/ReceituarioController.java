package br.com.asfecer.controller;

import br.com.asfecer.dao.ConsultaDAO;
import br.com.asfecer.dao.ItensreceituarioDAO;
import br.com.asfecer.dao.ReceituarioDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Itensreceituario;
import br.com.asfecer.model.Receituario;
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

@WebServlet(name = "ReceituarioController", urlPatterns = {"/criaReceituario.html", "/listaReceituarios.html", "/excluiReceituario.html", "/editaReceituario.html"})
public class ReceituarioController extends HttpServlet {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction utx;

    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().contains("criaReceituario.html")) {
            criarGet(request, response);
        } else if (request.getServletPath().contains("editaReceituario.html")) {
            editarGet(request, response);
        } else if (request.getServletPath().contains("listaReceituarios.html")) {
            listarGet(request, response);
        } else if (request.getServletPath().contains("excluiReceituario.html")) {
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(ReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(ReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaReceituarios.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/editaReceituario.html")) {
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (request.getServletPath().contains("/criaReceituario.html")) {
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(ReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ReceituarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroReceituario.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReceituarioDAO dao = new ReceituarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idReceituario"));
        Receituario receituario = dao.findReceituario(id);

        request.setAttribute("receituario", receituario);
        request.getRequestDispatcher("WEB-INF/views/editaReceituario.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Receituario> receituarios = new ArrayList<>();
        ReceituarioDAO dao = new ReceituarioDAO(utx, emf);
        receituarios = dao.findReceituarioEntities();

        request.setAttribute("receituarios", receituarios);
        request.getRequestDispatcher("WEB-INF/views/listaReceituario.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        ReceituarioDAO dao = new ReceituarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idReceituario"));
        dao.destroy(id);

        response.sendRedirect("listaReceituarios.html");
    }

    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {

        ReceituarioDAO dao = new ReceituarioDAO(utx, emf);
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ItensreceituarioDAO itensReceituario = new ItensreceituarioDAO(utx, emf);
        Receituario receituario = new Receituario();

        receituario.setData(formatDate.parse(request.getParameter("data")));
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        receituario.setConsulta(consulta);
        Itensreceituario ItensReceituario = itensReceituario.findItensreceituario(Integer.parseInt(request.getParameter("tipoReceituario")));
        receituario.setTipoReceituario(ItensReceituario);

        dao.create(receituario);

        response.sendRedirect("listaReceituarios.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, RollbackFailureException, Exception {

        ReceituarioDAO dao = new ReceituarioDAO(utx, emf);
        ConsultaDAO cons = new ConsultaDAO(utx, emf);
        ItensreceituarioDAO itensReceituario = new ItensreceituarioDAO(utx, emf);

        int idReceituario = Integer.parseInt(request.getParameter("idReceituario"));
        Receituario receituario = dao.findReceituario(idReceituario);
        receituario.setData(formatDate.parse(request.getParameter("data")));
        Consulta consulta = cons.findConsulta(Integer.parseInt(request.getParameter("consulta")));
        receituario.setConsulta(consulta);
        Itensreceituario ItensReceituario = itensReceituario.findItensreceituario(Integer.parseInt(request.getParameter("tipoReceituario")));
        receituario.setTipoReceituario(ItensReceituario);

        dao.edit(receituario);

        response.sendRedirect("listaReceituarios.html");
    }
}
