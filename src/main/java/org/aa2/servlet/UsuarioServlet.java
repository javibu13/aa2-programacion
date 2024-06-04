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
import java.security.MessageDigest;

@WebServlet("/usuario")
public class UsuarioServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Usuario usuario = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                return usuarioDao.getUsuarioById(Integer.parseInt(request.getParameter("id")));
            });
            request.setAttribute("usuario", usuario);
            request.setAttribute("rolUsuario", request.getSession(false).getAttribute("rolUsuario"));
            String stringStaticPath = request.getContextPath() + "/static/";
            request.setAttribute("staticPath", stringStaticPath);
            request.getRequestDispatcher("/detalleUsuario.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            throw new ServletException("Error obteniendo usuarios", e);
        }
    }
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("action").equals("create")) {
            // CREATE
            response.setContentType("text/html; charset=UTF-8");
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String contrasena = request.getParameter("contrasena");
            String rol = request.getParameter("rol");
            
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
                } else if (request.getSession(false).getAttribute("rolUsuario").equals("Usuario") && rol.equals("Administrador")) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No tienes permisos para crear un usuario administrador");
                } else {
                    final String avatarPath = (rol.equals("Administrador")) ? "static/img/admin.png" : "static/img/user.png";
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
        } else if (request.getParameter("action").equals("update")) {
            // UPDATE
            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String rol = request.getParameter("rol");

            try {
                boolean idExists = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.idExists(id);
                });

                boolean correoExists = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.correoExists(correo);
                });
                if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe el usuario indicado");
                } else if (request.getSession(false).getAttribute("rolUsuario").equals("Usuario") && Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.getUsuarioById(id).getRol();
                }).equals("Administrador")) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No tienes permisos para modificar un usuario administrador");
                } else if (correoExists && !Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.getUsuarioById(id).getCorreo().equals(correo);
                })) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Correo ya registrado");
                } else if (request.getSession(false).getAttribute("rolUsuario").equals("Usuario") && !rol.equals(Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.getUsuarioById(id).getRol();
                }))) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No tienes permisos para cambiar el rol del usuario");
                } else {
                    Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                        usuarioDao.updateUsuarioNoPass(id, nombre, correo, rol);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la actualización del usuario");
            }

        } else if (request.getParameter("action").equals("delete")) {
            // DELETE
            response.setContentType("text/html; charset=UTF-8");
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                boolean idExists = Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                    return usuarioDao.idExists(id);
                });
                if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe el usuario indicado");
                } else {
                    Database.getInstance().withExtension(UsuarioDao.class, usuarioDao -> {
                        usuarioDao.removeUsuario(id);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la eliminación del usuario");
            }
        }
    }
}