package br.com.asfecer.controller;

import br.com.asfecer.dao.CidadeDAO;
import br.com.asfecer.dao.ConvenioDAO;
import br.com.asfecer.dao.EnderecoDAO;
import br.com.asfecer.dao.EstadosDAO;
import br.com.asfecer.dao.PacienteDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Convenio;
import br.com.asfecer.model.Endereco;
import br.com.asfecer.model.Estados;
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

    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().contains("criaPaciente.html")) {
            criarGet(request, response);
        } else if (request.getServletPath().contains("editaPaciente.html")) {
            editarGet(request, response);
        } else if (request.getServletPath().contains("listaPacientes.html")) {
            listarGet(request, response);
        } else if (request.getServletPath().contains("excluiPaciente.html")) {
            try {
                excluirGet(request, response);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RuntimeException ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PacienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/editaPaciente.html")) {
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

        if (request.getServletPath().contains("/criaPaciente.html")) {
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
        List<Cidade> cidades = new ArrayList<>();
        List<Estados> estados = new ArrayList<>();
        List<Convenio> convenios = new ArrayList<>();
        CidadeDAO cid = new CidadeDAO(utx, emf);
        EstadosDAO est = new EstadosDAO(utx, emf);
        ConvenioDAO conv = new ConvenioDAO(utx, emf);
        estados = est.findEstadosEntities();
        cidades = cid.findCidadeEntities();
        convenios = conv.findConvenioEntities();

        request.setAttribute("cidades", cidades);
        request.setAttribute("estados", estados);
        request.setAttribute("convenios", convenios);
        request.getRequestDispatcher("WEB-INF/views/cadastroPaciente.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Cidade> cidades = new ArrayList<>();
            List<Estados> estados = new ArrayList<>();
            List<Convenio> convenios = new ArrayList<>();
            CidadeDAO cid = new CidadeDAO(utx, emf);
            EstadosDAO est = new EstadosDAO(utx, emf);
            ConvenioDAO conv = new ConvenioDAO(utx, emf);
            PacienteDAO dao = new PacienteDAO(utx, emf);
            int id = Integer.parseInt(request.getParameter("idPaciente"));
            Paciente paciente = dao.findPaciente(id);
            cidades = cid.findCidadeEntities();
            estados = est.findEstadosEntities();
            convenios = conv.findConvenioEntities();

            request.setAttribute("paciente", paciente);
            request.setAttribute("cidades", cidades);
            request.setAttribute("estados", estados);
            request.setAttribute("convenios", convenios);
            request.getRequestDispatcher("WEB-INF/views/editaPaciente.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("listaPaciente.html");
        }
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Paciente> pacientes = new ArrayList<>();
        PacienteDAO dao = new PacienteDAO(utx, emf);
        pacientes = dao.findPacienteEntities();

        request.setAttribute("paciente", pacientes);
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
        Convenio convenio = conv.findConvenio(Integer.parseInt(request.getParameter("convenio")));
        Date data = formato.parse(request.getParameter("dataNascimento"));

        endereco.setNomeNogradouro(request.getParameter("endereco"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
        endereco.setComplemento(request.getParameter("complemeto"));
        endereco.setTipoLogradouro(request.getParameter("tipo_endereço"));
        endereco.setCep(request.getParameter("cep"));
        endereco.setCidade(cidd.findCidade(Integer.parseInt(request.getParameter("cidade"))));

        ender.create(endereco);

        paciente.setNomepaciente(request.getParameter("nomePaciente"));
        paciente.setDatanascimento(data);
        paciente.setCpf(request.getParameter("cpf"));
        paciente.setCartaoconvenio(request.getParameter("cartaoConvenio"));
        paciente.setTiposanguineo(request.getParameter("tipoSanguineo"));
        paciente.setFatorrh(request.getParameter("fatorRH").charAt(0));
        paciente.setSexo(request.getParameter("sexo").charAt(0));
        paciente.setEmail(request.getParameter("email"));
        paciente.setTelefone(request.getParameter("telefone"));
        paciente.setCelular(request.getParameter("celular"));
        paciente.setConvenio(convenio);
        paciente.setObs(request.getParameter("obs"));
        paciente.setEndereco(endereco);
        paciente.setNaturalidadeCidade(cidd.findCidade(Integer.parseInt(request.getParameter("cidade"))));

        PacienteDAO dao = new PacienteDAO(utx, emf);

        dao.create(paciente);
        response.sendRedirect("listaPacientes.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException, NonexistentEntityException, RollbackFailureException, Exception {
        int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));
        PacienteDAO dao = new PacienteDAO(utx, emf);
        Paciente paciente = dao.findPaciente(idPaciente);

        ConvenioDAO conv = new ConvenioDAO(utx, emf);
        Endereco endereco = new Endereco();
        EnderecoDAO ender = new EnderecoDAO(utx, emf);
        CidadeDAO cidd = new CidadeDAO(utx, emf);
        
        Convenio convenio = conv.findConvenio(Integer.parseInt(request.getParameter("convenio")));

        endereco.setNomeNogradouro(request.getParameter("endereco"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
        endereco.setComplemento(request.getParameter("complemeto"));
        endereco.setTipoLogradouro(request.getParameter("tipo_endereço"));
        endereco.setCep(request.getParameter("cep"));
        endereco.setCidade(cidd.findCidade(Integer.parseInt(request.getParameter("cidade"))));

        ender.create(endereco);

        paciente.setNomepaciente(request.getParameter("nomePaciente"));
        paciente.setCpf(request.getParameter("cpf"));
        paciente.setCartaoconvenio(request.getParameter("cartaoConvenio"));
        paciente.setTiposanguineo(request.getParameter("tipoSanguineo"));
        paciente.setFatorrh(request.getParameter("fatorRH").charAt(0));
        paciente.setSexo(request.getParameter("sexo").charAt(0));
        paciente.setEmail(request.getParameter("email"));
        paciente.setTelefone(request.getParameter("telefone"));
        paciente.setCelular(request.getParameter("celular"));
        paciente.setConvenio(convenio);
        paciente.setObs(request.getParameter("obs"));
        paciente.setEndereco(endereco);
        paciente.setNaturalidadeCidade(cidd.findCidade(Integer.parseInt(request.getParameter("cidade"))));

        dao.edit(paciente);
        response.sendRedirect("listaPacientes.html");
    }
}
