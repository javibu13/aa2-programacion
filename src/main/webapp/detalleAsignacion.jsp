<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>
<main class="row">
    <div class="col-12 col-sm-9 col-md-8 m-auto">
        <div class="panel mt-4">
            <h1 class="text-center">Detalle de Asignación</h1>
            <form id="modificarForm" action="asignacion" method="put">
                <div class="mb-3">
                    <label for="id" class="form-label">Id:</label>
                    <input type="text" id="id" name="id" value="${asignacion.id}" class="form-control" disabled required maxlength="50">
                </div>
                <div class="mb-3">
                    <label for="productoId" class="form-label">Producto:</label>
                    <input type="text" id="productoId" name="productoId" value="${asignacion.elemento.productoId}" class="form-control" disabled required maxlength="255">
                </div>
                <div class="mb-3">
                    <label for="productoNombre" class="form-label">Nombre del Producto:</label>
                    <input type="text" id="productoNombre" name="productoNombre" value="${producto.nombre}" class="form-control" disabled required maxlength="255">
                </div>
                <div class="mb-3">
                    <label for="elementoId" class="form-label">Número de serie:</label>
                    <input type="text" id="elementoId" name="elementoId" value="${asignacion.elemento.numSerie}" class="form-control" disabled required maxlength="50">
                </div>
                <div class="mb-3">
                    <label for="usuarioId" class="form-label">Usuario:</label>
                    <input type="text" id="usuarioId" name="usuarioId" value="${asignacion.usuario.nombre}" class="form-control" disabled required maxlength="255">
                </div>
                <div class="mb-4">
                    <label for="fechaAsignacion" class="form-label fs-6"></label>
                    <input type="date" min="1970-01-01" value="${asignacion.fechaAsignacion}" class="form-control" id="fechaAsignacion" name="fechaAsignacion" required>
                </div>
                <div class="mb-4">
                    <label for="fechaDevolucion" class="form-label fs-6"></label>
                    <input type="date" min="1970-01-01" value="${asignacion.fechaDevolucion}" class="form-control" id="fechaDevolucion" name="fechaDevolucion">
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
            
            if (!window.confirm("¿Está seguro de que desea actualizar la asignación?")) {
                return;
            }

            const formData = "action=update&"+$(this).serialize()+"&id=${asignacion.id}";
            console.log(formData);
            $.ajax("asignacion", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/asignaciones";
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
            if (!window.confirm("¿Está seguro de que desea eliminar la asignación?")) {
                return;
            }
            $.ajax("asignacion", {
                type: "POST",
                data: "action=delete&id=${asignacion.id}",
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/asignaciones";
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