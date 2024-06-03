<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>
<main class="row">
    <div class="col-12 col-sm-9 col-md-8 m-auto">
        <div class="panel mt-4">
            <h1 class="text-center">Detalle de Elemento</h1>
            <form id="modificarForm" action="elemento" method="put">
                <div class="mb-3">
                    <label for="id" class="form-label">Id:</label>
                    <input type="text" id="id" name="id" value="${elemento.id}" class="form-control" disabled required maxlength="50">
                </div>
                <div class="mb-3">
                    <label for="productoId" class="form-label">Producto Id:</label>
                    <input type="text" id="productoId" name="productoId" value="${producto.id}" class="form-control" disabled required maxlength="50">
                </div>
                <div class="mb-3">
                    <label for="productonombre" class="form-label">Producto Nombre:</label>
                    <input type="text" id="productonombre" name="productonombre" value="${producto.nombre}" class="form-control" disabled required maxlength="255">
                </div>
                <div class="mb-3">
                    <label for="numSerie" class="form-label">Número de serie:</label>
                    <input type="text" id="numSerie" name="numSerie" value="${elemento.numSerie}" class="form-control" required maxlength="255">
                </div>
                <div class="mb-4">
                    <label for="estado" class="form-label">Estado:</label>
                    <select id="estado" name="estado" class="form-control" required>
                        <c:if test="${elemento.estado == 'Bueno'}">
                            <option value="Bueno" selected>Bueno</option>
                            <option value="Regular">Regular</option>
                            <option value="Malo">Malo</option>
                        </c:if>
                        <c:if test="${elemento.estado == 'Regular'}">
                            <option value="Bueno">Bueno</option>
                            <option value="Regular" selected>Regular</option>
                            <option value="Malo">Malo</option>
                        </c:if>
                        <c:if test="${elemento.estado == 'Malo'}">
                            <option value="Bueno">Bueno</option>
                            <option value="Regular">Regular</option>
                            <option value="Malo" selected>Malo</option>
                        </c:if>
                    </select>
                </div>
                <div id="reqResult"></div>
                <div class="justify-content-end d-flex">
                    <button id="modificarButton" type="submit" class="btn btn-warning ms-3">Modificar</button>
                    <button id="eliminarButton" type="button" class="btn btn-danger ms-3">Eliminar</button>
                </div>
            </form>
        </div>
    </div>
</main>

<script>
    $(document).ready(function() {
        $('#modificarForm').submit(function(event) {
            event.preventDefault();
            
            if (!window.confirm("¿Está seguro de que desea actualizar el elemento?")) {
                return;
            }

            const formData = "action=update&"+$(this).serialize()+"&id=${elemento.id}&productoId=${producto.id}";
            console.log(formData);
            $.ajax("elemento", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/elementos";
                    } else {
                        $("#reqResult").html("<div class='alert alert-danger' role='alert'>" + response + "</div>");
                    }
                },
                error: function(xhr) {
                    if (xhr.status === 409) {
                        console.log(xhr);
                        $("#reqResult").html("<div class='alert alert-danger' role='alert'>" + xhr.responseText + "</div>");
                    } else {
                        $("#reqResult").html("<div class='alert alert-danger' role='alert'>Error durante el registro.</div>");
                    }
                }
            });
        });

        $('#eliminarButton').click(function() {
            if (!window.confirm("¿Está seguro de que desea eliminar el elemento?")) {
                return;
            }
            $.ajax("elemento", {
                type: "POST",
                data: "action=delete&id=${elemento.id}",
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/elementos";
                    } else {
                        $("#reqResult").html("<div class='alert alert-danger' role='alert'>" + response + "</div>");
                    }
                },
                error: function(xhr) {
                    if (xhr.status === 409) {
                        console.log(xhr);
                        $("#reqResult").html("<div class='alert alert-danger' role='alert'>" + xhr.responseText + "</div>");
                    } else {
                        $("#reqResult").html("<div class='alert alert-danger' role='alert'>Error durante el registro.</div>");
                    }
                }
            });
        });
    });
</script>

</body>
</html>