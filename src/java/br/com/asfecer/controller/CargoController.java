package br.com.asfecer.controller;

import br.com.asfecer.dao.CargoDAO;
import br.com.asfecer.dao.DepartamentoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Cargo;
import br.com.asfecer.model.Departamento;
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

@WebServlet(name = "CargoController", urlPatterns = {"/criaCargo.html", "/listaCargos.html", "/excluiCargo.html", "/editaCargo.html"})
public class CargoController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaCargo.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaCargo.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaCargos.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiCargo.html")){
            try {
                excluirGet(request, response);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaCargos.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaCargo.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaCargo.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CargoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroCargo.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CargoDAO dao = new CargoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idCargo"));
        Cargo cargo = dao.findCargo(id);
        
        request.setAttribute("cargo", cargo);
        request.getRequestDispatcher("WEB-INF/views/editaCargo.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cargo> cargos = new ArrayList<>();
        CargoDAO dao = new CargoDAO(utx, emf);
        cargos = dao.findCargoEntities();
        
        request.setAttribute("cargos", cargos);
        request.getRequestDispatcher("WEB-INF/views/listaCargo.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        CargoDAO dao = new CargoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idCargo"));
        dao.destroy(id);
        
        response.sendRedirect("listaCargos.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        
        DepartamentoDAO dep = new DepartamentoDAO(utx, emf);
        CargoDAO dao = new CargoDAO(utx, emf);
        Cargo cargo = new Cargo();
        
        cargo.setCargo(request.getParameter("cargo"));
        Departamento depto = dep.findDepartamento(Integer.parseInt(request.getParameter("depto"))) ;
        cargo.setDepto(depto);
        
        dao.create(cargo);
        
        response.sendRedirect("listaCargos.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
        
        CargoDAO carg = new CargoDAO(utx, emf);
        DepartamentoDAO dep = new DepartamentoDAO(utx, emf);
        CargoDAO dao = new CargoDAO(utx, emf);
        
        int idCargo = Integer.parseInt(request.getParameter("idCargo"));
        Cargo cargo = dao.findCargo(idCargo);
        cargo.setCargo(request.getParameter("cargo"));
        Departamento depto = dep.findDepartamento(Integer.parseInt(request.getParameter("depto"))) ;
        cargo.setDepto(depto);
        
        dao.edit(cargo);
        
        response.sendRedirect("listaCargos.html");   
    }
}