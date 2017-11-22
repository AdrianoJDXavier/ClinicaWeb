package br.com.asfecer.controller;

import br.com.asfecer.dao.UsuarioDAO;
import br.com.asfecer.model.Usuario;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

@WebServlet(name = "LoginController", urlPatterns = {"/login.html", "/logoff.html"})
public class LoginController extends HttpServlet {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/logoff.html")) {
            Deslogar(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/login.html")) {
            Logar(request, response);
        }
    }

    private void Logar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        UsuarioDAO user = new UsuarioDAO(utx, emf);
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        List<Usuario> usuarios;
        Usuario usuario;
        usuarios = user.findUsuarioEntities();

        if (login.equals("admin") && senha.equals("admin")) {
            session.setAttribute("login", login);
            request.getRequestDispatcher("WEB-INF/views/index.jsp").forward(request, response);
        } else {
            for (Usuario usuario1 : usuarios) {
                if (login.equals(usuario1.getLogin()) && senha.equals(usuario1.getSenha())) {
                    usuario = usuario1;
                    session.setAttribute("usuario", usuario);
                    request.getRequestDispatcher("WEB-INF/views/index.jsp").forward(request, response);
                } else {
                    request.setAttribute("erro", "Login ou senha incorretos!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
        }

    }

    private void Deslogar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
