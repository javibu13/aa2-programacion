<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>
<main class="row">
    <div class="col-12 col-sm-9 col-md-8 m-auto">
        <div class="panel mt-4">
            <h1 class="text-center">Detalle de Producto</h1>
            <form id="modificarForm" action="producto" method="put">
                <div class="mb-3">
                    <label for="id" class="form-label">Id:</label>
                    <input type="text" id="id" name="id" value="${producto.id}" class="form-control" disabled required maxlength="50">
                </div>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="${producto.nombre}" class="form-control" required maxlength="255">
                </div>
                <div class="mb-3">
                    <label for="descripcion" class="form-label">Descripcion:</label>
                    <textarea type="text" id="descripcion" name="descripcion" class="form-control" required maxlength="500">${producto.descripcion}</textarea>
                </div>
                <div class="mb-3">
                    <label for="limiteUso" class="form-label">Límite de uso:</label>
                    <input type="text" id="limiteUso" name="limiteUso" value="${producto.limiteUso}" class="form-control" required>
                </div>
                <div class="mb-4">
                    <label for="numElementos" class="form-label">Número de elementos de este tipo:</label>
                    <input type="text" id="numElementos" name="numElementos" value="${producto.numElementos}" class="form-control" disabled required>
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
            
            if (!window.confirm("¿Está seguro de que desea actualizar el producto?")) {
                return;
            }

            const formData = "action=update&"+$(this).serialize()+"&id=${producto.id}";
            console.log(formData);
            $.ajax("producto", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/productos";
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
            if (!window.confirm("¿Está seguro de que desea eliminar el producto?")) {
                return;
            }
            $.ajax("producto", {
                type: "POST",
                data: "action=delete&id=${producto.id}",
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/productos";
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