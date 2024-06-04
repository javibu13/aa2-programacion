package org.aa2.servlet;

import org.aa2.dao.Database;
import org.aa2.dao.AsignacionDao;
import org.aa2.domain.Asignacion;
import org.aa2.dao.UsuarioDao;
import org.aa2.domain.Usuario;
import org.aa2.dao.ElementoDao;
import org.aa2.domain.Elemento;
import org.aa2.dao.ProductoDao;
import org.aa2.domain.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/asignacion")
public class AsignacionServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Asignacion asignacion = Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> {
                return asignacionDao.getAsignacionById(Integer.parseInt(request.getParameter("id")));
            });
            asignacion.linkRelatedData();
            Elemento elemento = asignacion.getElemento();
            Producto producto = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                return productoDao.getProductoById(elemento.getProductoId());
            });
            List<Elemento> elementos = Database.getInstance().withExtension(ElementoDao.class, ElementoDao::getAllElementos);
            List<Usuario> usuarios = Database.getInstance().withExtension(UsuarioDao.class, UsuarioDao::getAllUsuarios);
            request.setAttribute("asignacion", asignacion);
            request.setAttribute("producto", producto);
            request.setAttribute("elementos", elementos);
            request.setAttribute("usuarios", usuarios);
            String stringStaticPath = request.getContextPath() + "/static/";
            request.setAttribute("staticPath", stringStaticPath);
            request.getRequestDispatcher("/detalleAsignacion.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            throw new ServletException("Error obteniendo asignaciones", e);
        }
    }
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("action").equals("create")) {
            // CREATE
            response.setContentType("text/html; charset=UTF-8");
            String elementoId = request.getParameter("elementoId");
            String usuarioId = request.getParameter("usuarioId");
            String fechaAsignacion = request.getParameter("fechaAsignacion");

            try {
                boolean elementoExists = Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                    return elementoDao.idExists(Integer.parseInt(elementoId));
                });
                boolean usuarioExists = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.idExists(Integer.parseInt(usuarioId));
                });
                boolean asignacionExists = Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> {
                    return asignacionDao.asignacionElementoActivaExists(Integer.parseInt(elementoId));
                });
                
                if (!elementoExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un elemento con ese Id");
                } else if (!usuarioExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un usuario con ese Id");
                } else if (asignacionExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Ya existe una asignación activa para ese elemento");
                } else {
                    Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> {
                        asignacionDao.addAsignacion(Integer.parseInt(elementoId), Integer.parseInt(usuarioId), Date.valueOf(fechaAsignacion));
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la creación de la asignación");
            }
        } else if (request.getParameter("action").equals("update")) {
            // UPDATE
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            String id = request.getParameter("id");
            String fechaAsignacion = request.getParameter("fechaAsignacion");
            String fechaDevolucion = request.getParameter("fechaDevolucion");
            try {
                boolean idExists = Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> {
                    return asignacionDao.idExists(Integer.parseInt(id));
                });
                
                boolean fechasCorrectas = true;
                if (!fechaDevolucion.equals("")) {
                    fechasCorrectas = Date.valueOf(fechaAsignacion).before(Date.valueOf(fechaDevolucion));
                }
                
                if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe una asignación con ese Id");
                } else if (!fechasCorrectas) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("La fecha de devolución debe ser posterior a la fecha de asignación");
                } else {
                    Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> {
                        asignacionDao.updateAsignacionFechas(Integer.parseInt(id), Date.valueOf(fechaAsignacion), !fechaDevolucion.equals("") ? Date.valueOf(fechaDevolucion) : null);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la actualización de la asignación");
            }
        } else if (request.getParameter("action").equals("delete")) {
            // DELETE
            response.setContentType("text/html; charset=UTF-8");
            String id = request.getParameter("id");
            try {
                boolean idExists = Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> {
                    return asignacionDao.idExists(Integer.parseInt(id));
                });
                if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un producto con ese Id");
                } else {
                    Database.getInstance().withExtension(AsignacionDao.class, asignacionDao -> {
                        asignacionDao.removeAsignacion(Integer.parseInt(id));
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la eliminación de la asignación");
            }
        }
    }
}