package br.com.asfecer.controller;

import br.com.asfecer.dao.CidadeDAO;
import br.com.asfecer.dao.ConvenioDAO;
import br.com.asfecer.dao.EnderecoDAO;
import br.com.asfecer.dao.PacienteDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Convenio;
import br.com.asfecer.model.Endereco;
import br.com.asfecer.model.Paciente;
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

@WebServlet(name = "PacienteController", urlPatterns = {"/criaPaciente.html", "/listaPacientes.html", "/excluiPaciente.html", "/editaPaciente.html"})
public class PacienteController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaPaciente.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaPaciente.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaPacientes.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiPaciente.html")){
            excluirGet(request, response);
            response.sendRedirect("listaPacientes.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaPaciente.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaPaciente.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/cadastroPaciente.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PacienteDAO dao = new PacienteDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPaciente"));
        Paciente paciente = dao.findPaciente(id);
        
        request.setAttribute("paciente", paciente);
        request.getRequestDispatcher("WEB-INF/editaPaciente.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Paciente> pacientes = new ArrayList<>();
        PacienteDAO dao = new PacienteDAO(utx, emf);
        pacientes = dao.findPacienteEntities();
        
        request.setAttribute("pacientes", pacientes);
        request.getRequestDispatcher("WEB-INF/listaPaciente.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        PacienteDAO dao = new PacienteDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPaciente"));
        dao.destroy(id);
        
        response.sendRedirect("listaPacientes.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        ConvenioDAO conv = new ConvenioDAO(utx, emf);
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        CidadeDAO cidd = new CidadeDAO(utx, emf);
        
        String nomePaciente = request.getParameter("nomePaciente");
        Date dataNascimento = formatDate.parse(request.getParameter("dataNascimento"));
        String nomeMae = request.getParameter("nomeMae");
        String cpf = request.getParameter("cpf");
        String cartaoConvenio = request.getParameter("cartaoConvenio");
        String tipoSanguineo = request.getParameter("tipoSanguineo");
        Character fatorRH = request.getParameter("fatorRH").charAt(0);
        Character sexo = request.getParameter("sexo").charAt(0);
        String email  = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String celular = request.getParameter("celular");
        String obs = request.getParameter("obs");
        Convenio convenio = conv.findConvenio(Integer.parseInt(request.getParameter("convenio"))) ;
        Endereco endereco = ender.findEndereco(Integer.parseInt(request.getParameter("endereco")));
        Cidade naturalidadeCidade = cidd.findCidade(Integer.parseInt(request.getParameter("naturalidadeCidade")));
        
        Paciente paciente = new Paciente(nomePaciente, dataNascimento, nomeMae, cpf, cartaoConvenio, tipoSanguineo, fatorRH, sexo, email, telefone, celular, obs, convenio, endereco, naturalidadeCidade);
        PacienteDAO dao = new PacienteDAO(utx, emf);
        
        dao.create(paciente);
        
        response.sendRedirect("listaPacientes.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        ConvenioDAO conv = new ConvenioDAO(utx, emf);
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        CidadeDAO cidd = new CidadeDAO(utx, emf);
        
        int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));
        String nomePaciente = request.getParameter("nomePaciente");
        Date dataNascimento = formatDate.parse(request.getParameter("dataNascimento"));
        String nomeMae = request.getParameter("nomeMae");
        String cpf = request.getParameter("cpf");
        String cartaoConvenio = request.getParameter("cartaoConvenio");
        String tipoSanguineo = request.getParameter("tipoSanguineo");
        Character fatorRH = request.getParameter("fatorRH").charAt(0);
        Character sexo = request.getParameter("sexo").charAt(0);
        String email  = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String celular = request.getParameter("celular");
        String obs = request.getParameter("obs");
        Convenio convenio = conv.findConvenio(Integer.parseInt(request.getParameter("convenio"))) ;
        Endereco endereco = ender.findEndereco(Integer.parseInt(request.getParameter("endereco")));
        Cidade naturalidadeCidade = cidd.findCidade(Integer.parseInt(request.getParameter("naturalidadeCidade")));
        
        Paciente paciente = new Paciente(idPaciente, nomePaciente, dataNascimento, nomeMae, cpf, cartaoConvenio, tipoSanguineo, fatorRH, sexo, email, telefone, celular, obs, convenio, endereco, naturalidadeCidade);       
        
        PacienteDAO dao = new PacienteDAO(utx, emf);
        
        dao.edit(paciente);
        
        response.sendRedirect("listaPacientes.html");   
    }
}