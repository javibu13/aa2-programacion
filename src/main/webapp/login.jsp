<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<script type="text/javascript">
    $(document).ready(function() {
        $("form").on("submit", async event => {
            event.preventDefault();
            const passwordField = document.getElementById('floatingPassword');
            const password = passwordField.value;

            // Convierte la contraseña a un ArrayBuffer
            const encoder = new TextEncoder();
            const data = encoder.encode(password);

            // Calcula el hash SHA-1
            const hashBuffer = await crypto.subtle.digest('SHA-1', data);

            // Convierte el ArrayBuffer a una cadena hexadecimal
            const hashArray = Array.from(new Uint8Array(hashBuffer));
            const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');

            const formDataTmp = $("#loginForm").serialize();
            const formData = formDataTmp.split("contrasena=")[0] + "contrasena=" + hashHex;

            $.ajax("login", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/productos";
                    } else {
                        $("#result").html("<div class='alert alert-danger' role='alert'>Algo salió mal.</div>");
                    }
                },
                error: function(xhr) {
                    if (xhr.status === 409) {
                        $("#result").html("<div class='alert alert-danger' role='alert'>Credenciales no válidas.</div>");
                    } else {
                        $("#result").html("<div class='alert alert-danger' role='alert'>Error durante el inicio de sesión.</div>");
                    }
                }
            });
        });
    });
</script>

<body>
    <header class="mb-5 mt-3">
        <div class="text-center">
            <h1 class="display-1"><a href="login" class="no-link"><i class="bi bi-cone-striped"></i> GESTOR DE EPI <i class="bi bi-hammer"></i></a></h1>
        </div>
    </header>
    <main class="row">
        <div class="col-12 col-sm-9 col-md-6 m-auto">
            <div class="panel">
                <form id="loginForm" action="login" method="post">
                    <h3 class="mb-3 fw-normal">Identificarse</h3>

                    <div class="form-floating mb-3">
                        <input type="email" name="correo" class="form-control" id="floatingEmail" placeholder="nombre@ejemplo.es">
                        <label for="floatingEmail">Correo</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="password" name="contrasena" class="form-control" id="floatingPassword" placeholder="Contraseña">
                        <label for="floatingPassword">Contraseña</label>
                    </div>

                    <button class="btn btn-lg btn-primary w-100" type="submit">Iniciar sesión</button>
                </form>
                <div class="mt-3">
                    <a href="register">Registrarse</a>
                </div>
                <br/>
                <div id="result"></div>
            </div>
        </div>
    </main>
</body>
</html>