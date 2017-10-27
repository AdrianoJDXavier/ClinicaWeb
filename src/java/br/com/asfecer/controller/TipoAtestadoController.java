package br.com.asfecer.controller;

import br.com.asfecer.dao.MedicamentoDAO;
import br.com.asfecer.dao.PatologiaDAO;
import br.com.asfecer.dao.TipoatestadoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Medicamento;
import br.com.asfecer.model.Patologia;
import br.com.asfecer.model.Tipoatestado;
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

@WebServlet(name = "TipoAtestadoController", urlPatterns = {"/criaTipoAtestado.html", "/listaTipoAtestados.html", "/excluiTipoAtestado.html", "/editaTipoAtestado.html"})
public class TipoAtestadoController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaTipoAtestado.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaTipoAtestado.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaTipoAtestados.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiTipoAtestado.html")){
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaTipoAtestados.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaTipoAtestado.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaTipoAtestado.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(TipoAtestadoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroTipoAtestado.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TipoatestadoDAO dao = new TipoatestadoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idTipoAtestado"));
        Tipoatestado tipoAtestado = dao.findTipoatestado(id);
        
        request.setAttribute("tipoAtestado", tipoAtestado);
        request.getRequestDispatcher("WEB-INF/views/editaTipoAtestado.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tipoatestado> tipoAtestados = new ArrayList<>();
        TipoatestadoDAO dao = new TipoatestadoDAO(utx, emf);
        tipoAtestados = dao.findTipoatestadoEntities();
        
        request.setAttribute("tipoAtestados", tipoAtestados);
        request.getRequestDispatcher("WEB-INF/views/listaTipoAtestado.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        TipoatestadoDAO dao = new TipoatestadoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idTipoAtestado"));
        dao.destroy(id);
        
        response.sendRedirect("listaTipoAtestados.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, RollbackFailureException, Exception {
        
        MedicamentoDAO mednto = new MedicamentoDAO(utx, emf);
        PatologiaDAO pat = new PatologiaDAO(utx, emf);
        TipoatestadoDAO dao = new TipoatestadoDAO(utx, emf);
        Tipoatestado tipoAtestado = new Tipoatestado();
               
        tipoAtestado.setDescricao(request.getParameter("descricao"));
        tipoAtestado.setLocalafastamento(request.getParameter("localAfastamento"));
        tipoAtestado.setDias(Integer.parseInt(request.getParameter("dias")));
        tipoAtestado.setAtividade(request.getParameter("atividade"));
        Medicamento medicamento = mednto.findMedicamento(Integer.parseInt(request.getParameter("medicamento")));
        tipoAtestado.setMedicamento(medicamento);
        Patologia patologia = pat.findPatologia(Integer.parseInt(request.getParameter("patologia")));
        tipoAtestado.setPatologia(patologia);
        
        
        dao.create(tipoAtestado);
        
        response.sendRedirect("listaTipoAtestados.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
        
        
        MedicamentoDAO mednto = new MedicamentoDAO(utx, emf);
        PatologiaDAO pat = new PatologiaDAO(utx, emf);
        TipoatestadoDAO dao = new TipoatestadoDAO(utx, emf);
               
        int idTipoAtestado = Integer.parseInt(request.getParameter("idTipoAtestado"));
        Tipoatestado tipoAtestado = dao.findTipoatestado(idTipoAtestado);
        tipoAtestado.setDescricao(request.getParameter("descricao"));
        tipoAtestado.setLocalafastamento(request.getParameter("localAfastamento"));
        tipoAtestado.setDias(Integer.parseInt(request.getParameter("dias")));
        tipoAtestado.setAtividade(request.getParameter("atividade"));
        Medicamento medicamento = mednto.findMedicamento(Integer.parseInt(request.getParameter("medicamento")));
        tipoAtestado.setMedicamento(medicamento);
        Patologia patologia = pat.findPatologia(Integer.parseInt(request.getParameter("patologia")));
        tipoAtestado.setPatologia(patologia);
        
        dao.edit(tipoAtestado);
        
        response.sendRedirect("listaTipoAtestados.html");   
    }
}