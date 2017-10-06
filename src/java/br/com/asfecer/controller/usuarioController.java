package br.com.asfecer.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class usuarioController extends HttpServlet {

 
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession session = request.getSession(true);
        if (session.getAttribute("usuario") == null) {
            request.getRequestDispatcher("WEB-INF/views/cadastroUsuario.jsp").forward(request, response);
        } else {
            request.setAttribute("erro", "Login ou senha incorretos!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }
}


