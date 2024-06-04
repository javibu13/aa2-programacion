<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>

<div class="container">
    <h1 class="mt-5">Lista de Usuarios</h1>
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

    <table id="usuariosTable" class="" data-toggle="table" data-pagination="true" data-sortable="true"> <!-- data-search="true" data-search-highlight="true"  -->
        <thead class="table-dark">
        <tr>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="id" data-formatter="idFormatter">Id</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="nombre">Nombre</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="correo">Correo</th>
            <th data-align="center" data-halign="center" data-sortable="false" data-field="rol">Rol</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="usuario" items="${usuarios}">
            <tr>
                <td>${usuario.id}</td> <!-- <a href="${pageContext.request.contextPath}/usuario?id=${usuario.id}">${usuario.id}</a> FORMATEADO CON BOOTSTRAP TABLE -->
                <td>${usuario.nombre}</td>
                <td>${usuario.correo}</td>
                <td>${usuario.rol}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal -->
<div class="modal fade" id="createNewModal" tabindex="-1" aria-labelledby="createNewModalLabel" aria-hidden="true">
    <form id="crearNuevoForm" action="usuario" method="post">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="createNewModal">Nuevo usuario</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-4">
                        <label for="nombre" class="form-label">Nombre</label>
                        <input type="text" class="form-control" id="nombre" name="nombre" required maxlength="255">
                    </div>
                    <div class="mb-4">
                        <label for="correo" class="form-label">Correo</label>
                        <input type="text" class="form-control" id="correo" name="correo" required maxlength="255">
                    </div>
                    <div class="mb-4">
                        <label for="contrasena" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="contrasena" name="contrasena" required maxlength="255">
                    </div>
                    <div class="mb-4">
                        <label for="rol" class="form-label fs-6">Rol</label>
                        <select class="form-select" id="rol" name="rol" required>
                            <option value="Usuario">Usuario</option>
                            <option value="Administrador">Administrador</option>
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
        $('#crearNuevoForm').submit(async function(event) {
            event.preventDefault();

            const passwordField = document.getElementById('contrasena');
            const password = passwordField.value;

            // Convierte la contraseña a un ArrayBuffer
            const encoder = new TextEncoder();
            const data = encoder.encode(password);

            // Calcula el hash SHA-1
            const hashBuffer = await crypto.subtle.digest('SHA-1', data);

            // Convierte el ArrayBuffer a una cadena hexadecimal
            const hashArray = Array.from(new Uint8Array(hashBuffer));
            const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');

            const formData = "action=create&" + "nombre=" + $(this).find("#nombre").val() + "&correo=" + $(this).find("#correo").val() + "&rol=" + $(this).find("#rol").val() + "&contrasena=" + hashHex;

            $.ajax("usuario", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/usuarios";
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
        return '<a href="${pageContext.request.contextPath}/usuario?id=' + value + '">' + value + '</a>';
    }

    $(document).ready(function() {
        $('#searchInput').on('keyup', function() {
            const value = $(this).val();
            $.ajax("usuarios", {
                type: "POST",
                data: {search: value},
                success: function(response) {
                    $("#usuariosTable").bootstrapTable('load', response);
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
