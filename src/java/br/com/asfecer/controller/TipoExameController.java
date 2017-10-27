package br.com.asfecer.controller;

import br.com.asfecer.dao.TipoexameDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Tipoexame;
import java.io.IOException;
import java.text.ParseException;
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

@WebServlet(name = "TipoExameController", urlPatterns = {"/criaTipoExame.html", "/listaTipoExames.html", "/excluiTipoExame.html", "/editaTipoExame.html"})
public class TipoExameController extends HttpServlet {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().contains("criaTipoExame.html")) {
            criarGet(request, response);
        } else if (request.getServletPath().contains("editaTipoExame.html")) {
            editarGet(request, response);
        } else if (request.getServletPath().contains("listaTipoExames.html")) {
            listarGet(request, response);
        } else if (request.getServletPath().contains("excluiTipoExame.html")) {
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaTipoExames.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/editaTipoExame.html")) {
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (request.getServletPath().contains("/criaTipoExame.html")) {
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TipoExameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroTipoExame.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TipoexameDAO dao = new TipoexameDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idTipoExame"));
        Tipoexame tipoExame = dao.findTipoexame(id);

        request.setAttribute("tipoExame", tipoExame);
        request.getRequestDispatcher("WEB-INF/views/editaTipoExame.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tipoexame> tipoExames = new ArrayList<>();
        TipoexameDAO dao = new TipoexameDAO(utx, emf);
        tipoExames = dao.findTipoexameEntities();

        request.setAttribute("tipoExames", tipoExames);
        request.getRequestDispatcher("WEB-INF/views/listaTipoExame.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        TipoexameDAO dao = new TipoexameDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idTipoExame"));
        dao.destroy(id);

        response.sendRedirect("listaTipoExames.html");
    }

    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {

        Tipoexame tipoExame = new Tipoexame();
        tipoExame.setTipoexame(request.getParameter("tipoExame"));

        TipoexameDAO dao = new TipoexameDAO(utx, emf);

        dao.create(tipoExame);

        response.sendRedirect("listaTipoExames.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {

        TipoexameDAO dao = new TipoexameDAO(utx, emf);

        int idTipoExame = Integer.parseInt(request.getParameter("idTipoExame"));
        Tipoexame tipoExame = dao.findTipoexame(idTipoExame);
        tipoExame.setTipoexame(request.getParameter("tipoExame"));

        dao.edit(tipoExame);

        response.sendRedirect("listaTipoExames.html");
    }
}
