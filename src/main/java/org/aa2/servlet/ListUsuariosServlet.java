package org.aa2.servlet;

import org.aa2.dao.Database;
import org.aa2.dao.UsuarioDao;
import org.aa2.domain.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/usuarios")
public class ListUsuariosServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<Usuario> usuarios = Database.getInstance().withExtension(UsuarioDao.class, UsuarioDao::getAllUsuarios);
            request.setAttribute("usuarios", usuarios);
            String stringStaticPath = request.getContextPath() + "/static/";
            request.setAttribute("staticPath", stringStaticPath);
            request.getRequestDispatcher("/listaUsuarios.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            throw new ServletException("Error obteniendo usuarios", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String filtro = request.getParameter("search");
        try {
            List<Usuario> usuariosFiltrados = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> usuarioDao.getUsuariosByFilter(filtro));
            String jsonResponse = new Gson().toJson(usuariosFiltrados);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error obteniendo usuarios\"}");
        }
    }

}