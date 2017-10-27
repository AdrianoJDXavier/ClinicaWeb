package br.com.asfecer.controller;

import br.com.asfecer.dao.AgendaDAO;
import br.com.asfecer.dao.CargoDAO;
import br.com.asfecer.dao.CidadeDAO;
import br.com.asfecer.dao.ConsultaDAO;
import br.com.asfecer.dao.ConvenioDAO;
import br.com.asfecer.dao.EnderecoDAO;
import br.com.asfecer.dao.EstadosDAO;
import br.com.asfecer.dao.FuncionarioDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Agenda;
import br.com.asfecer.model.Cargo;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Convenio;
import br.com.asfecer.model.Endereco;
import br.com.asfecer.model.Estados;
import br.com.asfecer.model.Funcionario;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
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

@WebServlet(name = "FuncionarioController", urlPatterns = {"/criaFuncionario.html", "/listaFuncionarios.html", "/excluiFuncionario.html", "/editaFuncionario.html"})
public class FuncionarioController extends HttpServlet {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction utx;

    public SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().contains("criaFuncionario.html")) {
            criarGet(request, response);
        } else if (request.getServletPath().contains("editaFuncionario.html")) {
            editarGet(request, response);
        } else if (request.getServletPath().contains("listaFuncionarios.html")) {
            listarGet(request, response);
        } else if (request.getServletPath().contains("excluiFuncionario.html")) {
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect("listaFuncionarios.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/editaFuncionario.html")) {
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (request.getServletPath().contains("/criaFuncionario.html")) {
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroFuncionario.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idFuncionario"));
        Funcionario funcionario = dao.findFuncionario(id);

        request.setAttribute("funcionario", funcionario);
        request.getRequestDispatcher("WEB-INF/views/editaFuncionario.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Funcionario> funcionarios = new ArrayList<>();
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);
        funcionarios = dao.findFuncionarioEntities();

        request.setAttribute("funcionarios", funcionarios);
        request.getRequestDispatcher("WEB-INF/views/listaFuncionario.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idFuncionario"));
        dao.destroy(id);

        response.sendRedirect("listaFuncionarios.html");
    }

    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {

        CargoDAO carg = new CargoDAO(utx, emf);
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);
        Funcionario funcionario = new Funcionario();

        funcionario.setNomefuncionario(request.getParameter("nomeFuncionario"));
        funcionario.setDatanascimento(formatDate.parse(request.getParameter("dataNascimento")));
        funcionario.setCpf(request.getParameter("cpf"));
        funcionario.setRg(request.getParameter("rg"));
        funcionario.setOrgaoemissor(request.getParameter("orgaoEmissor"));
        funcionario.setCtps(request.getParameter("ctps"));
        funcionario.setPis(request.getParameter("pis"));
        funcionario.setEmail(request.getParameter("email"));
        funcionario.setTelefone(request.getParameter("telefone"));
        funcionario.setCelular(request.getParameter("celular"));
        funcionario.setObs(request.getParameter("obs"));
        Cargo cargo = carg.findCargo(Integer.parseInt(request.getParameter("cargo")));
        funcionario.setCargo(cargo);
        Endereco endereco = ender.findEndereco(Integer.parseInt(request.getParameter("endereco")));
        funcionario.setEndereco(endereco);
        Estados ufEmissor = uf.findEstados(request.getParameter("ufEmissor"));
        funcionario.setUfEmissor(ufEmissor);

        dao.create(funcionario);

        response.sendRedirect("listaFuncionarios.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, RollbackFailureException, Exception {

        CargoDAO carg = new CargoDAO(utx, emf);
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);

        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        Funcionario funcionario = dao.findFuncionario(idFuncionario);
        funcionario.setNomefuncionario(request.getParameter("nomeFuncionario"));
        funcionario.setDatanascimento(formatDate.parse(request.getParameter("dataNascimento")));
        funcionario.setCpf(request.getParameter("cpf"));
        funcionario.setRg(request.getParameter("rg"));
        funcionario.setOrgaoemissor(request.getParameter("orgaoEmissor"));
        funcionario.setCtps(request.getParameter("ctps"));
        funcionario.setPis(request.getParameter("pis"));
        funcionario.setEmail(request.getParameter("email"));
        funcionario.setTelefone(request.getParameter("telefone"));
        funcionario.setCelular(request.getParameter("celular"));
        funcionario.setObs(request.getParameter("obs"));
        Cargo cargo = carg.findCargo(Integer.parseInt(request.getParameter("cargo")));
        funcionario.setCargo(cargo);
        Endereco endereco = ender.findEndereco(Integer.parseInt(request.getParameter("endereco")));
        funcionario.setEndereco(endereco);
        Estados ufEmissor = uf.findEstados(request.getParameter("ufEmissor"));
        funcionario.setUfEmissor(ufEmissor);

        dao.edit(funcionario);

        response.sendRedirect("listaFuncionarios.html");
    }
}
