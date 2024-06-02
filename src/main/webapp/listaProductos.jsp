<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>

<div class="container">
    <h1 class="mt-5">Lista de Productos</h1>
    <div class="navbar">
        <div class="justify-content-start">
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createNewModal">
            Crear Nuevo
            </button>
        </div>
        <div class="justify-content-end">
            <input type="text" class="form-control" id="searchInput" placeholder="Buscar...">
        </div>
    </div>

    <table id="productosTable" class="" data-toggle="table" data-pagination="true" data-sortable="true"> <!-- data-search="true" data-search-highlight="true"  -->
        <thead class="table-dark">
        <tr>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="id" data-formatter="idFormatter">Id</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="nombre">Nombre</th>
            <th                     data-halign="center" data-sortable="false" data-field="descripcion">Descripción</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="limiteUso">Límite de uso (meses)</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="numElementos">Número de Elementos</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="producto" items="${productos}">
            <tr>
                <td>${producto.id}</td> <!-- <a href="${pageContext.request.contextPath}/producto?id=${producto.id}">${producto.id}</a> FORMATEADO CON BOOTSTRAP TABLE -->
                <td>${producto.nombre}</td>
                <td>${producto.descripcion}</td>
                <td>${producto.limiteUso}</td>
                <td>${producto.numElementos}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal -->
<div class="modal fade" id="createNewModal" tabindex="-1" aria-labelledby="createNewModalLabel" aria-hidden="true">
    <form id="crearNuevoForm" action="producto" method="post">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="createNewModal">Nuevo producto</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-4">
                    <label for="id" class="form-label">Id</label>
                    <input type="text" class="form-control" id="id" name="id" required maxlength="50">
                </div>
                <div class="mb-4">
                    <label for="nombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" required maxlength="255">
                </div>
                <div class="mb-4">
                    <label for="descripcion" class="form-label">Descripción</label>
                    <textarea type="text" class="form-control" id="descripcion" name="descripcion" required maxlength="500"></textarea>
                </div>
                <div class="mb-4">
                    <label for="limiteUso" class="form-label fs-6">Límite de uso (meses)</label>
                    <input type="number" class="form-control" id="limiteUso" name="limiteUso" required min="0">
                </div>
                <div id="crearNuevoResult" class="mb-3">
                    
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="submit" class="btn btn-primary">Crear nuevo</button>
            </div>
            </div>
        </div>
    </form>
</div>

<script>
    $(document).ready(function() {
        $('#crearNuevoForm').submit(function(event) {
            event.preventDefault();
            const formData = "action=create&"+$(this).serialize();
            $.ajax("producto", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/productos";
                    } else {
                        $("#crearNuevoResult").html("<div class='alert alert-danger' role='alert'>" + response + "</div>");
                    }
                },
                error: function(xhr) {
                    if (xhr.status === 409) {
                        console.log(xhr);
                        $("#crearNuevoResult").html("<div class='alert alert-danger' role='alert'>" + xhr.responseText + "</div>");
                    } else {
                        $("#crearNuevoResult").html("<div class='alert alert-danger' role='alert'>Error durante el registro.</div>");
                    }
                }
            });
        });
    });
</script>

<script>
    function idFormatter(value) {
        return '<a href="${pageContext.request.contextPath}/producto?id=' + value + '">' + value + '</a>';
    }

    $(document).ready(function() {
        $('#searchInput').on('keyup', function() {
            const value = $(this).val();
            $.ajax("productos", {
                type: "POST",
                data: {search: value},
                success: function(response) {
                    $("#productosTable").bootstrapTable('load', response);
                },
                error: function(xhr) {
                    console.log(xhr);
                }
            });
            
        });
    });
</script>

</body>
</html>
