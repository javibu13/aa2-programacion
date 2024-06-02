<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>
<main class="row">
    <div class="col-12 col-sm-9 col-md-8 m-auto">
        <div class="panel mt-4">
            <h1 class="text-center">Detalle de Usuario</h1>
            <form id="modificarForm" action="usuario" method="put">
                <div class="mb-3">
                    <label for="id" class="form-label">Id:</label>
                    <input type="text" id="id" name="id" value="${usuario.id}" class="form-control" disabled required maxlength="50">
                </div>
                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="${usuario.nombre}" class="form-control" required maxlength="255">
                </div>
                <div class="mb-3">
                    <label for="correo" class="form-label">Correo:</label>
                    <input type="text" id="correo" name="correo" value="${usuario.correo}" class="form-control" required maxlength="255">
                </div>
                <div class="mb-3">
                    <label for="rol" class="form-label">Rol:</label>
                    <select id="rol" name="rol" class="form-control" required>
                        <c:if test="${usuario.rol == 'Usuario'}">
                            <option value="Usuario" selected> Usuario</option>
                            <option value="Administrador">Administrador</option>
                        </c:if>
                        <c:if test="${usuario.rol == 'Administrador'}">
                            <option value="Usuario"> Usuario</option>
                            <option value="Administrador" selected>Administrador</option>
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
            
            if (!window.confirm("¿Está seguro de que desea actualizar el usuario?")) {
                return;
            }

            const formData = "action=update&"+$(this).serialize()+"&id=${usuario.id}";

            $.ajax("usuario", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/usuarios";
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
            if (!window.confirm("¿Está seguro de que desea eliminar el usuario?")) {
                return;
            }
            $.ajax("usuario", {
                type: "POST",
                data: "action=delete&id=${usuario.id}",
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/usuarios";
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