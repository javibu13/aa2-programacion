package org.aa2.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        
        try {
            boolean correoExists = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                return usuarioDao.correoExists(correo);
            });

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = messageDigest.digest(nombre.getBytes());
            // Convert the byte array into a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            String hashedNombre = sb.toString();
            
            if (correoExists) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Correo ya registrado");
            } else if (hashedNombre.equals(contrasena)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("La contraseña no puede ser igual al nombre");
            } else {
                boolean primerUsuario = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.getUsuariosCount() == 0;
                });
                final String rol; // Declare the variable as final
                final String avatarPath; // Declare the variable as final
                if (primerUsuario) {
                    avatarPath = "static/img/admin.png";
                    rol = "Administrador";
                } else {
                    avatarPath = "static/img/user.png";
                    rol = "Usuario";
                }
                Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    usuarioDao.addUsuario(nombre, correo, contrasena, avatarPath, rol);
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

