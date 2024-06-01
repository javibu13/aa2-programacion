package org.aa2.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aa2.dao.Database;
import org.aa2.dao.UsuarioDao;
import org.aa2.domain.Usuario;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("headTitle", "Login");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error loading login view", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        
        try {
            boolean correoExists = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                return usuarioDao.correoExists(correo);
            });

            if (correoExists) {
                Usuario usuarioFromDB = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.getUsuarioByCorreo(correo);
                });
                boolean userAndPassCorrect = usuarioFromDB.getPassword().equals(contrasena);
                if (userAndPassCorrect) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");

                    HttpSession session = request.getSession(true);
                    session.setAttribute("idUsuario", usuarioFromDB.getId());
                    session.setAttribute("rolUsuario", usuarioFromDB.getRol());
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Contraseña incorrecta");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Correo no registrado");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error durante el inicio de sesión");
        }
    }
}