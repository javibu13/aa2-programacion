package org.aa2.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.aa2.dao.Database;
import org.aa2.dao.UsuarioDao;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            request.setAttribute("headTitle", "Register");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error loading login view", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String avatarPath = request.getParameter("avatarPath");
        
        try {
            boolean correoExists = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                return usuarioDao.correoExists(correo);
            });

            if (correoExists) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Correo ya registrado");
            } else {
                Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    usuarioDao.addUsuario(nombre, correo, password, avatarPath, "Usuario");
                    return null;
                });
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("success");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error durante el registro");
        }
    }
}

