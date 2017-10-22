package br.com.asfecer.controller;

import br.com.asfecer.dao.CidadeDAO;
import br.com.asfecer.dao.EnderecoDAO;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Endereco;
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

@WebServlet(name = "EnderecoController", urlPatterns = {"/criaEndereco.html", "/listaEnderecos.html", "/excluiEndereco.html", "/editaEndereco.html"})
public class EnderecoController extends HttpServlet {
    
    @PersistenceUnit
    private EntityManagerFactory emf;
    
    @Resource
    private UserTransaction utx;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(request.getServletPath().contains("criaEndereco.html")){
            criarGet(request, response);
        }else if(request.getServletPath().contains("editaEndereco.html")){
            editarGet(request, response);
        }else if(request.getServletPath().contains("listaEnderecos.html")){
            listarGet(request, response);
        }else if(request.getServletPath().contains("excluiEndereco.html")){
            excluirGet(request, response);
            response.sendRedirect("listaEnderecos.html");
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if(request.getServletPath().contains("/editaEndereco.html")){
            try {
                editarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(EnderecoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(request.getServletPath().contains("/criaEndereco.html")){
            try {
                criarPost(request, response);
            } catch (ParseException ex) {
                Logger.getLogger(EnderecoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void criarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/cadastroEndereco.jsp").forward(request, response);
    }

    private void editarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EnderecoDAO dao = new EnderecoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idEndereco"));
        Endereco endereco = dao.findEndereco(id);
        
        request.setAttribute("endereco", endereco);
        request.getRequestDispatcher("WEB-INF/views/editaEndereco.jsp").forward(request, response);
    }

    private void listarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Endereco> enderecos = new ArrayList<>();
        EnderecoDAO dao = new EnderecoDAO(utx, emf);
        enderecos = dao.findEnderecoEntities();
        
        request.setAttribute("enderecos", enderecos);
        request.getRequestDispatcher("WEB-INF/views/listaEndereco.jsp").forward(request, response);
    }

    private void excluirGet(HttpServletRequest request, HttpServletResponse response) throws IOException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EnderecoDAO dao = new EnderecoDAO(utx, emf);
        int id = Integer.parseInt(request.getParameter("idEndereco"));
        dao.destroy(id);
        
        response.sendRedirect("listaEnderecos.html");
    }
 
    private void criarPost(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        
        CidadeDAO cdd = new CidadeDAO(utx, emf);
                
        String tipoLogradouro = request.getParameter("tipoLogradouro");
        String nomeLogradouro = request.getParameter("nomeLogradouro");
        int numero = Integer.parseInt(request.getParameter("numero"));
        String complemento = request.getParameter("complemento");
        String bairro = request.getParameter("bairro");
        String cep = request.getParameter("cep");
        Cidade cidade = cdd.findCidade(Integer.parseInt(request.getParameter("cidade")));
        
        Endereco endereco = new Endereco(tipoLogradouro, nomeLogradouro, numero, complemento, bairro, cep, cidade);
        EnderecoDAO dao = new EnderecoDAO(utx, emf);
        
        dao.create(endereco);
        
        response.sendRedirect("listaEnderecos.html");
    }

    private void editarPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        
        CidadeDAO cdd = new CidadeDAO(utx, emf);
                
        int idEndereco = Integer.parseInt(request.getParameter("idEndereco"));
        String tipoLogradouro = request.getParameter("tipoLogradouro");
        String nomeLogradouro = request.getParameter("nomeLogradouro");
        int numero = Integer.parseInt(request.getParameter("numero"));
        String complemento = request.getParameter("complemento");
        String bairro = request.getParameter("bairro");
        String cep = request.getParameter("cep");
        Cidade cidade = cdd.findCidade(Integer.parseInt(request.getParameter("cidade")));
        
        Endereco endereco = new Endereco(idEndereco, tipoLogradouro, nomeLogradouro, numero, complemento, bairro, cep, cidade);
        EnderecoDAO dao = new EnderecoDAO(utx, emf);
        
        dao.edit(endereco);
        
        response.sendRedirect("listaEnderecos.html");   
    }
}