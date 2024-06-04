package org.aa2.servlet;

import org.aa2.dao.Database;
import org.aa2.dao.AsignacionDao;
import org.aa2.domain.Asignacion;
import org.aa2.dao.ElementoDao;
import org.aa2.domain.Elemento;
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

@WebServlet("/asignaciones")
public class ListAsignacionesServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<Asignacion> asignaciones = Database.getInstance().withExtension(AsignacionDao.class, AsignacionDao::getAllAsignaciones);
            asignaciones.forEach(asignacion -> {
                try {
                    asignacion.linkRelatedData();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<Elemento> elementos = Database.getInstance().withExtension(ElementoDao.class, ElementoDao::getAllElementos);
            List<Usuario> usuarios = Database.getInstance().withExtension(UsuarioDao.class, UsuarioDao::getAllUsuarios);
            request.setAttribute("asignaciones", asignaciones);
            request.setAttribute("elementos", elementos);
            request.setAttribute("usuarios", usuarios);
            String stringStaticPath = request.getContextPath() + "/static/";
            request.setAttribute("staticPath", stringStaticPath);
            request.getRequestDispatcher("/listaAsignaciones.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            throw new ServletException("Error obteniendo asignaciones", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String filtro = request.getParameter("search");
        try {
            List<Asignacion> asignacionesFiltradas = Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> asignacionDao.getAsignacionesByFilter(filtro));
            asignacionesFiltradas.forEach(asignacion -> {
                try {
                    asignacion.linkRelatedData();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            String jsonResponse = new Gson().toJson(asignacionesFiltradas);
            System.out.println(jsonResponse);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error obteniendo asignaciones\"}");
        }
    }
}