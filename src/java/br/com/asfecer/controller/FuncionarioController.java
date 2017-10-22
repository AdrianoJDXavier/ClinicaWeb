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
        if(request.getServletPath().contains("criaFuncionario.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaFuncionario.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaFuncionarios.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiFuncionario.html")){
            excluirGet(request, response);
            response.sendRedirect("listaFuncionarios.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaFuncionario.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(FuncionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaFuncionario.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
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

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idFuncionario"));
        dao.destroy(id);
        
        response.sendRedirect("listaFuncionarios.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        CargoDAO carg = new CargoDAO(utx, emf);
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        
        String nomeFuncionario = request.getParameter("nomeFuncionario");
        Date dataNascimento = formatDate.parse(request.getParameter("dataNascimento"));
        String cpf = request.getParameter("cpf");
        String rg = request.getParameter("rg");
        String orgaoEmissor = request.getParameter("orgaoEmissor");
        String ctps = request.getParameter("ctps");
        String pis = request.getParameter("pis");
        String email  = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String celular = request.getParameter("celular");
        String obs = request.getParameter("obs");
        Cargo cargo = carg.findCargo(Integer.parseInt(request.getParameter("cargo")));
        Endereco endereco = ender.findEndereco(Integer.parseInt(request.getParameter("endereco")));
        Estados ufEmissor = uf.findEstados(request.getParameter("ufEmissor"));
        
        Funcionario funcionario = new Funcionario(nomeFuncionario, dataNascimento, cpf, rg, orgaoEmissor, ctps, pis, email, telefone, celular, obs, cargo, endereco, ufEmissor);
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);
        
        dao.create(funcionario);
        
        response.sendRedirect("listaFuncionarios.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        CargoDAO carg = new CargoDAO(utx, emf);
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        EstadosDAO uf = new EstadosDAO(utx, emf);
        
        int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
        String nomeFuncionario = request.getParameter("nomeFuncionario");
        Date dataNascimento = formatDate.parse(request.getParameter("dataNascimento"));
        String cpf = request.getParameter("cpf");
        String rg = request.getParameter("rg");
        String orgaoEmissor = request.getParameter("orgaoEmissor");
        String ctps = request.getParameter("ctps");
        String pis = request.getParameter("pis");
        String email  = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String celular = request.getParameter("celular");
        String obs = request.getParameter("obs");
        Cargo cargo = carg.findCargo(Integer.parseInt(request.getParameter("cargo")));
        Endereco endereco = ender.findEndereco(Integer.parseInt(request.getParameter("endereco")));
        Estados ufEmissor = uf.findEstados(request.getParameter("ufEmissor"));
        
        Funcionario funcionario = new Funcionario(idFuncionario, nomeFuncionario, dataNascimento, cpf, rg, orgaoEmissor, ctps, pis, email, telefone, celular, obs, cargo, endereco, ufEmissor);     
        FuncionarioDAO dao = new FuncionarioDAO(utx, emf);
        
        dao.edit(funcionario);
        
        response.sendRedirect("listaFuncionarios.html");   
    }
}