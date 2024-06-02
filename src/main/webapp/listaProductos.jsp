<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>

<div class="container">
    <h1 class="mt-5">Lista de Productos</h1>
    <table class="" data-toggle="table" data-pagination="true" data-search="true" data-search-highlight="true" data-sortable="true">
        <thead class="table-dark">
        <tr>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="id">Id</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="nombre">Nombre</th>
            <th                     data-halign="center" data-sortable="false" data-field="descripcion">Descripción</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="limiteUso">Límite de uso (meses)</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="numElementos">Número de Elementos</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="producto" items="${productos}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/producto?id=${producto.id}">${producto.id}</a></td>
                <td>${producto.nombre}</td>
                <td>${producto.descripcion}</td>
                <td>${producto.limiteUso}</td>
                <td>${producto.numElementos}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
