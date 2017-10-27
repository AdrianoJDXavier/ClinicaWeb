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
    
    SimpleDateFormat formatDate;  

    public PacienteController() {
        this.formatDate = new SimpleDateFormat("dd/MM/yyyy");
    }

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
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            } catch (RollbackFailureException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaPaciente.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cidade cid = new Cidade();
        
        request.setAttribute("cidade", cid);
        request.getRequestDispatcher("WEB-INF/views/cadastroPaciente.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PacienteDAO dao = new PacienteDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPaciente"));
        Paciente paciente = dao.findPaciente(id);
        
        request.setAttribute("paciente", paciente);
        request.getRequestDispatcher("WEB-INF/views/editaPaciente.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Paciente> pacientes = new ArrayList<>();
        PacienteDAO dao = new PacienteDAO(utx, emf);
        pacientes = dao.findPacienteEntities();
        
        request.setAttribute("pacientes", pacientes);
        request.getRequestDispatcher("WEB-INF/views/listarPacientes.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException, Exception {
        PacienteDAO dao = new PacienteDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idPaciente"));
        dao.destroy(id);
        
        response.sendRedirect("listaPacientes.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, Exception {
        
        Paciente paciente = new Paciente();
        ConvenioDAO conv = new ConvenioDAO(utx, emf);
        Endereco endereco = new Endereco();
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        CidadeDAO cidd = new CidadeDAO(utx, emf);
        
        paciente.setNomepaciente(request.getParameter("nomePaciente"));
        paciente.setDatanascimento(formatDate.parse(request.getParameter("dataNascimento")));
        paciente.setNomemae(request.getParameter("nomeMae"));
        paciente.setCpf(request.getParameter("cpf"));
        paciente.setCartaoconvenio(request.getParameter("cartaoConvenio"));
        paciente.setTiposanguineo(request.getParameter("tipoSanguineo"));
        paciente.setFatorrh(request.getParameter("fatorRH").charAt(0));
        paciente.setSexo(request.getParameter("sexo").charAt(0));
        paciente.setEmail(request.getParameter("email"));
        paciente.setTelefone(request.getParameter("telefone"));
        paciente.setCelular(request.getParameter("celular"));
        paciente.setObs(request.getParameter("obs"));
        endereco.setNomeNogradouro(request.getParameter("endereco"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
        endereco.setComplemento(request.getParameter("complemeto"));
        
        ender.create(endereco);
        Convenio convenio = conv.findConvenio(Integer.parseInt(request.getParameter("convenio"))) ;
        Cidade cidade = cidd.findCidade(Integer.parseInt(request.getParameter("naturalidadeCidade")));
        
        PacienteDAO dao = new PacienteDAO(utx, emf);
        
        dao.create(paciente);
        
        response.sendRedirect("listaPacientes.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
        
        ConvenioDAO conv = new ConvenioDAO(utx, emf);
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        CidadeDAO cidd = new CidadeDAO(utx, emf);
        PacienteDAO dao = new PacienteDAO(utx, emf);
        
        int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));
        int idEndereco = Integer.parseInt(request.getParameter("idPaciente"));
        int idCidade = Integer.parseInt(request.getParameter("idPaciente"));
        int idConvenio = Integer.parseInt(request.getParameter("idPaciente"));
        Endereco endereco = ender.findEndereco(idEndereco);
        Convenio convenio = conv.findConvenio(idConvenio);
        Cidade cidade = cidd.findCidade(idCidade);
        Paciente paciente = dao.findPaciente(idPaciente);
        paciente.setNomepaciente(request.getParameter("nomePaciente"));
        paciente.setDatanascimento(formatDate.parse(request.getParameter("dataNascimento")));
        paciente.setNomemae(request.getParameter("nomeMae"));
        paciente.setCpf(request.getParameter("cpf"));
        paciente.setCartaoconvenio(request.getParameter("cartaoConvenio"));
        paciente.setTiposanguineo(request.getParameter("tipoSanguineo"));
        paciente.setFatorrh(request.getParameter("fatorRH").charAt(0));
        paciente.setSexo(request.getParameter("sexo").charAt(0));
        paciente.setEmail(request.getParameter("email"));
        paciente.setTelefone(request.getParameter("telefone"));
        paciente.setCelular(request.getParameter("celular"));
        paciente.setObs(request.getParameter("obs"));
        endereco.setNomeNogradouro(request.getParameter("endereco"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
        endereco.setComplemento(request.getParameter("complemeto"));
        convenio.setIdconvenio(idConvenio);
        cidade.setIdcidade(idCidade);
        
        cidd.edit(cidade);
        conv.edit(convenio);
        ender.edit(endereco);
        dao.edit(paciente);
        
        response.sendRedirect("listaPacientes.html");   
    }
}