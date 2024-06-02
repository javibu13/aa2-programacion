<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>

<div class="container">
    <h1 class="mt-5">Lista de Elementos</h1>
    <div class="navbar">
        <div class="justify-content-start">
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#createNewModal">
            Crear Nuevo
            </button>
        </div>
        <div class="justify-content-end">
            <input type="text" class="form-control" id="searchInput" placeholder="Buscar...">
        </div>
    </div>

    <table id="elementosTable" class="" data-toggle="table" data-pagination="true" data-sortable="true"> <!-- data-search="true" data-search-highlight="true"  -->
        <thead class="table-dark">
        <tr>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="id" data-formatter="idFormatter">Id</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="productoId">Producto ID</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="numSerie">Número de Serie</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="estado">Estado</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="elemento" items="${elementos}">
            <tr>
                <td>${elemento.id}</td> <!-- <a href="${pageContext.request.contextPath}/elemento?id=${elemento.id}">${elemento.id}</a> FORMATEADO CON BOOTSTRAP TABLE -->
                <td>${elemento.productoId}</td>
                <td>${elemento.numSerie}</td>
                <td>${elemento.estado}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal -->
<div class="modal fade" id="createNewModal" tabindex="-1" aria-labelledby="createNewModalLabel" aria-hidden="true">
    <form id="crearNuevoForm" action="elemento" method="post">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="createNewModal">Nuevo elemento</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-4">
                    <label for="productoId" class="form-label">Producto ID</label>
                    <select class="form-select" id="productoId" name="productoId" required>
                        <option value="">Seleccione un producto</option>
                        <c:forEach var="producto" items="${productos}">
                            <option value="${producto.id}">(${producto.id}) ${producto.nombre}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-4">
                    <label for="numSerie" class="form-label">Número de Serie</label>
                    <input type="text" class="form-control" id="numSerie" name="numSerie" required maxlength="255">
                </div>
                <div class="mb-4">
                    <label for="estado" class="form-label fs-6">Estado</label>
                    <select class="form-select" id="estado" name="estado" required>
                        <option value="Bueno">Bueno</option>
                        <option value="Regular">Regular</option>
                        <option value="Malo">Malo</option>
                    </select>
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
            $.ajax("elemento", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/elementos";
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
        return '<a href="${pageContext.request.contextPath}/elemento?id=' + value + '">' + value + '</a>';
    }

    $(document).ready(function() {
        $('#searchInput').on('keyup', function() {
            const value = $(this).val();
            $.ajax("elementos", {
                type: "POST",
                data: {search: value},
                success: function(response) {
                    $("#elementosTable").bootstrapTable('load', response);
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
