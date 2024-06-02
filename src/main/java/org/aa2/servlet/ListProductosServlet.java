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
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/productos")
public class ListProductosServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<Producto> productos = Database.getInstance().withExtension(ProductoDao.class, ProductoDao::getAllProductosCountElementos);
            request.setAttribute("productos", productos);
            String stringStaticPath = request.getContextPath() + "/static/";
            request.setAttribute("staticPath", stringStaticPath);
            request.getRequestDispatcher("/listaProductos.jsp").forward(request, response);
        } catch (Exception e){
            e.printStackTrace();
            throw new ServletException("Error obteniendo productos", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String filtro = request.getParameter("search");
        try {
            List<Producto> productosFiltrados = Database.getInstance().withExtension(ProductoDao.class, productoDao -> productoDao.getProductosByFilter(filtro));
            String jsonResponse = new Gson().toJson(productosFiltrados);
            response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error obteniendo productos\"}");
        }
    }

}