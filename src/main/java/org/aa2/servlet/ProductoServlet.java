package org.aa2.servlet;

import org.aa2.dao.Database;
import org.aa2.dao.ProductoDao;
import org.aa2.domain.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/producto")
public class ProductoServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Producto producto = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                return productoDao.getProductoById(request.getParameter("id"));
            });
            request.setAttribute("producto", producto);
            String stringStaticPath = request.getContextPath() + "/static/";
            request.setAttribute("staticPath", stringStaticPath);
            request.getRequestDispatcher("/detalleProducto.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            throw new ServletException("Error obteniendo productos", e);
        }
        }
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("action").equals("create")) {
            // CREATE
            response.setContentType("text/html; charset=UTF-8");
            String id = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            int limiteUso = Integer.parseInt(request.getParameter("limiteUso"));

            try {
                boolean idExists = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.idExists(id);
                });
    
                boolean nombreExists = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.nombreExists(nombre);
                });
                
                if (idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Ya existe un producto con ese Id");
                } else if (nombreExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Ya existe un producto con ese Nombre");
                } else if (limiteUso < 0 || limiteUso > 240) {  // 240 meses = 20 años
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("El límite de uso debe ser un número entre 0 y 240 meses (20 años)");
                } else {
                    Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                        productoDao.addProducto(id, nombre, descripcion, limiteUso);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la creación del producto");
            }
        } else if (request.getParameter("action").equals("update")) {
            // UPDATE
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            String id = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            int limiteUso = Integer.parseInt(request.getParameter("limiteUso"));
            
            try {
                boolean idExists = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.idExists(id);
                });

                boolean nombreExists = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.nombreExists(nombre);
                });

                if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un producto con ese Id");
                } else if (nombreExists && !Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.getProductoById(id).getNombre().equals(nombre);
                })) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Ya existe un producto con ese Nombre");
                } else if (limiteUso < 0 || limiteUso > 240) {  // 240 meses = 20 años
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("El límite de uso debe ser un número entre 0 y 240 meses (20 años)");
                } else {
                    Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                        productoDao.updateProducto(id, nombre, descripcion, limiteUso);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la actualización del producto");
            }
        } else if (request.getParameter("action").equals("delete")) {
            // DELETE
            response.setContentType("text/html; charset=UTF-8");
            String id = request.getParameter("id");
            try {
                boolean idExists = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.idExists(id);
                });
                if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un producto con ese Id");
                } else {
                    Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                        productoDao.removeProducto(id);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la eliminación del producto");
            }
        }
    }
}