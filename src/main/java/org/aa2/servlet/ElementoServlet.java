package org.aa2.servlet;

import org.aa2.dao.Database;
import org.aa2.dao.ElementoDao;
import org.aa2.dao.ProductoDao;
import org.aa2.domain.Elemento;
import org.aa2.domain.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/elemento")
public class ElementoServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Elemento elemento = Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                return elementoDao.getElementoById(Integer.parseInt(request.getParameter("id")));
            });
            Producto producto = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                return productoDao.getProductoById(elemento.getProductoId());
            });
            request.setAttribute("elemento", elemento);
            request.setAttribute("producto", producto);
            String stringStaticPath = request.getContextPath() + "/static/";
            request.setAttribute("staticPath", stringStaticPath);
            request.getRequestDispatcher("/detalleElemento.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            throw new ServletException("Error obteniendo elementos", e);
        }
        }
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("action").equals("create")) {
            // CREATE
            response.setContentType("text/html; charset=UTF-8");
            String productoId = request.getParameter("productoId");
            String numSerie = request.getParameter("numSerie");
            String estado = request.getParameter("estado");

            try {
                boolean productoIdExists = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.idExists(productoId);
                });
                boolean numSerieForProductIdExists = Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                    return elementoDao.numSerieForProductIdExists(productoId, numSerie);
                });
                if (!productoIdExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un producto con ese Id");
                } else if (numSerieForProductIdExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Ya existe un elemento con ese Número de Serie para ese Producto");
                } else if (!estado.equals("Bueno") && !estado.equals("Regular") && !estado.equals("Malo")) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("El estado debe ser 'Bueno', 'Regular' o 'Malo'");
                } else {
                    Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                        elementoDao.addElemento(productoId, numSerie, estado);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la creación del elemento");
            }
        } else if (request.getParameter("action").equals("update")) {
            // UPDATE
            response.setContentType("text/html; charset=UTF-8");
            int id = Integer.parseInt(request.getParameter("id"));
            String productoId = request.getParameter("productoId");
            String numSerie = request.getParameter("numSerie");
            String estado = request.getParameter("estado");

            try {
                boolean idExists = Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                    return elementoDao.idExists(id);
                });
                boolean productoIdExists = Database.getInstance().withExtension(ProductoDao.class, productoDao -> {
                    return productoDao.idExists(productoId);
                });
                boolean numSerieForProductIdExists = Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                    return elementoDao.numSerieForProductIdExists(productoId, numSerie);
                });
                if (!productoIdExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un producto con ese Id");
                } else if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un elemento con ese Id");
                } else if (numSerieForProductIdExists && !Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                    return elementoDao.getElementoById(id).getNumSerie().equals(numSerie);
                })) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Ya existe un elemento con ese Número de Serie para ese Producto");
                } else if (!estado.equals("Bueno") && !estado.equals("Regular") && !estado.equals("Malo")) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("El estado debe ser 'Bueno', 'Regular' o 'Malo'");
                } else {
                    Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                        elementoDao.updateElementoNumSerieEstado(id, numSerie, estado);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la actualización del elemento");
            }

        } else if (request.getParameter("action").equals("delete")) {
            // DELETE
            response.setContentType("text/html; charset=UTF-8");
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                boolean idExists = Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                    return elementoDao.idExists(id);
                });
                if (!idExists) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("No existe un elemento con ese Id");
                } else {
                    Database.getInstance().withExtension(ElementoDao.class, elementoDao -> {
                        elementoDao.removeElemento(id);
                        return null;
                    });
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("success");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error durante la eliminación del elemento");
            }
        }
    }
}