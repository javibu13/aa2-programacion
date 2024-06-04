<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<script type="text/javascript">
    $(document).ready(function() {
        $("form").on("submit", async function(event) {
            event.preventDefault();
            
            if ($("#newPassword").val() != $("#newPassword2").val()) {
                $("#result").html("<div class='alert alert-danger' role='alert'>" + "Las " + "</div>");
                return;
            }

            const passwordFieldCurrent = document.getElementById('currentPassword');
            const passwordFieldNew = document.getElementById('newPassword');
            const passwordCurrent = passwordFieldCurrent.value;
            const passwordNew = passwordFieldNew.value;

            // Convierte la contraseña a un ArrayBuffer
            const encoder1 = new TextEncoder();
            const encoder2 = new TextEncoder();
            const data1 = encoder1.encode(passwordCurrent);
            const data2 = encoder2.encode(passwordNew);

            // Calcula el hash SHA-1
            const hashBuffer1 = await crypto.subtle.digest('SHA-1', data1);
            const hashBuffer2 = await crypto.subtle.digest('SHA-1', data2);

            // Convierte el ArrayBuffer a una cadena hexadecimal
            const hashArray1 = Array.from(new Uint8Array(hashBuffer1));
            const hashArray2 = Array.from(new Uint8Array(hashBuffer2));
            const hashHex1 = hashArray1.map(b => b.toString(16).padStart(2, '0')).join('');
            const hashHex2 = hashArray2.map(b => b.toString(16).padStart(2, '0')).join('');

            const formData = "&currentPassword=" + hashHex1 + "&newPassword=" + hashHex2;
            $.ajax("perfil", {
                type: "POST",
                data: formData,
                success: function(response) {
                        $("#result").html("<div class='alert alert-success' role='alert'>" + "Contraseña actualizada" + "</div>");
                },
                error: function(xhr) {
                    if (xhr.status === 409) {
                        console.log(xhr);
                        $("#result").html("<div class='alert alert-danger' role='alert'>" + xhr.responseText + "</div>");
                    } else {
                        $("#result").html("<div class='alert alert-danger' role='alert'>" + "Algo salió mal..." + "</div>");
                    }
                }
            });
        });
    });
</script>

<body>
<%@ include file="/includes/headerBar.jsp" %>
<main class="container my-4">
    <h1 class="text-center mb-4">Perfil</h1>

    <div class="form-group mb-3">
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" class="form-control" value="${usuario.nombre}" readonly disabled>
    </div>
    <div class="form-group mb-3">
        <label for="correo">Correo:</label>
        <input type="text" id="correo" name="correo" class="form-control" value="${usuario.correo}" readonly disabled>
    </div>
    <div class="form-group mb-3">
        <label for="rol">Rol:</label>
        <input type="email" id="rol" name="rol" class="form-control" value="${usuario.rol}" readonly disabled>
    </div>
    <br>
    <br>
    <br>
    <form id="changePass" action="changePassword" method="POST" class="mb-3">
        <div class="form-group mb-3">
            <label for="currentPassword">Contraseña actual:</label>
            <input type="password" class="form-control" id="currentPassword" name="currentPassword" required>
        </div>
        <div class="form-group mb-3">
            <label for="newPassword">Nueva contraseña:</label>
            <input type="password" class="form-control" id="newPassword" name="newPassword" required>
        </div>
        <div class="form-group mb-3">
            <label for="newPassword2">Confirmar contraseña:</label>
            <input type="password" class="form-control" id="newPassword2" name="newPassword2" required>
        </div>
        <div id="result"></div>
        <button type="submit" class="btn btn-success">Cambiar contraseña</button>
    </form>
</main>
