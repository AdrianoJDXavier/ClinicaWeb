package br.com.asfecer.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/login.html"})
public class LoginController extends HttpServlet {

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        if (login.equals("admin") && senha.equals("admin")) {
            session.setAttribute("login", login);
            request.getRequestDispatcher("WEB-INF/views/index.jsp").forward(request, response);
        }else{
            request.setAttribute("erro", "Login ou senha incorretos!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

}
