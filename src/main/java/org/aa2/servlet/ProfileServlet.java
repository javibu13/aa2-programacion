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
}
