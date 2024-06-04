package org.aa2.servlet;

import org.aa2.dao.Database;
import org.aa2.dao.UsuarioDao;
import org.aa2.domain.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/perfil")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        try {
            Usuario usuario = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> usuarioDao.getUsuarioById(idUsuario));
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error obteniendo el perfil", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");

        try {
            if (Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> usuarioDao.checkPassword(idUsuario, currentPassword))) {
                if (newPassword != null && !newPassword.isEmpty()) {
                    Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                        usuarioDao.updateUsuarioPass(idUsuario, newPassword);
                        return null;
                    });
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("La nueva contraseña no puede estar vacía");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("Contraseña actual incorrecta");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error durante el cambio de contraseña");
        }
    }
}
