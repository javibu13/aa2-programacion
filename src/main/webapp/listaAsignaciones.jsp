<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/includes/commonStartDoc.jsp" %>

<body>
<%@ include file="/includes/headerBar.jsp" %>

<div class="container">
    <h1 class="mt-5">Lista de Asignaciones</h1>
    <div class="navbar">
        <div class="justify-content-start">
            <!-- Button trigger modal -->
            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#createNewModal">
            Crear Nueva
            </button>
        </div>
        <div class="justify-content-end">
            <input type="text" class="form-control" id="searchInput" placeholder="Buscar...">
        </div>
    </div>

    <table id="asignacionesTable" class="" data-toggle="table" data-pagination="true" data-sortable="true"> <!-- data-search="true" data-search-highlight="true"  -->
        <thead class="table-dark">
        <tr>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="id" data-formatter="idFormatter">Id</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="productoId" data-formatter="productoIdFormatter">Producto</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="numSerie" data-formatter="elementoIdFormatter">Número de serie</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="usuarioNombre" data-formatter="usuarioIdFormatter">Usuario</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="fechaAsignacion">Fecha de Asignación</th>
            <th data-align="center" data-halign="center" data-sortable="true" data-field="fechaDevolucion">Fecha de Devolución</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="asignacion" items="${asignaciones}">
            <tr>
                <td>${asignacion.id}</td> <!-- <a href="${pageContext.request.contextPath}/asignacion?id=${asignacion.id}">${asignacion.id}</a> FORMATEADO CON BOOTSTRAP TABLE -->
                <td>${asignacion.elemento.productoId}</td>
                <td>${asignacion.elemento.id}|${asignacion.elemento.numSerie}</td>
                <td>${asignacion.usuario.id}|${asignacion.usuario.nombre}</td>
                <td>${asignacion.fechaAsignacion}</td>
                <c:if test="${asignacion.fechaDevolucion != null}">
                    <td>${asignacion.fechaDevolucion}</td>
                </c:if>
                <c:if test="${asignacion.fechaDevolucion == null}">
                    <td>—</td>
                </c:if>
            </tr>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal -->
<div class="modal fade" id="createNewModal" tabindex="-1" aria-labelledby="createNewModalLabel" aria-hidden="true">
    <form id="crearNuevoForm" action="asignacion" method="post">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="createNewModal">Nueva asignación</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-4">
                    <label for="elementoId" class="form-label">Elemento</label>
                    <select class="form-select" id="elementoId" name="elementoId" required>
                        <option value="">Seleccione un elemento</option>
                        <c:forEach var="elemento" items="${elementos}">
                            <option value="${elemento.id}">${elemento.productoId} - ${elemento.numSerie}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-4">
                    <label for="usuarioId" class="form-label">Usuario</label>
                    <select class="form-select" id="usuarioId" name="usuarioId" required>
                        <option value="">Seleccione un usuario</option>
                        <c:forEach var="usuario" items="${usuarios}">
                            <option value="${usuario.id}">${usuario.nombre} - ${usuario.correo}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-4">
                    <label for="fechaAsignacion" class="form-label fs-6"></label>
                    <input type="date" min="1970-01-01" class="form-control" id="fechaAsignacion" name="fechaAsignacion" required>
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
            $.ajax("asignacion", {
                type: "POST",
                data: formData,
                success: function(response) {
                    if (response === "success") {
                        window.location.href = "/aa2/asignaciones";
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
        return '<a href="${pageContext.request.contextPath}/asignacion?id=' + value + '">' + value + '</a>';
    }

    function productoIdFormatter(value) {
        return '<a href="${pageContext.request.contextPath}/producto?id=' + value + '">' + value + '</a>';
    }

    function elementoIdFormatter(value) {
        const id = value.split("|")[0];
        const numSerie = value.split("|")[1];
        return '<a href="${pageContext.request.contextPath}/elemento?id=' + id + '">' + numSerie + '</a>';
    }

    function usuarioIdFormatter(value) {
        const id = value.split("|")[0];
        const nombre = value.split("|")[1];
        return '<a href="${pageContext.request.contextPath}/usuario?id=' + id + '">' + nombre + '</a>';
    }

    function transformDate(dateString) {
        // Crear un objeto Date a partir del string original
        let date = new Date(dateString);

        // Obtener el año, mes y día del objeto Date
        let year = date.getFullYear();
        let month = ("0" + (date.getMonth() + 1)).slice(-2); // Los meses son indexados desde 0
        let day = ("0" + date.getDate()).slice(-2);

        // Formatear en "YYYY/MM/DD"
        let formattedDate = year + "-" + month + "-" + day;

        return formattedDate;
    }

    $(document).ready(function() {
        $('#searchInput').on('keyup', function() {
            const value = $(this).val();
            $.ajax("asignaciones", {
                type: "POST",
                data: {search: value},
                success: async function(response) {
                    const asignaciones = [];
                    await response.forEach(asignacion => {
                        // "id" data-formatter="idFormatter">Id</th>
                        // "productoId" data-formatter="productoIdFormatter">Producto</th>
                        // "numSerie" data-formatter="elementoIdFormatter">Número de serie</th>
                        // "usuarioNombre" data-formatter="usuarioIdFormatter">Usuario</th>
                        // "fechaAsignacion">Fecha de Asignación</th>
                        // "fechaDevolucion">Fecha de Devolución</th>

                        // <td>${asignacion.id}</td> <!-- <a href="${pageContext.request.contextPath}/asignacion?id=${asignacion.id}">${asignacion.id}</a> FORMATEADO CON BOOTSTRAP TABLE -->
                        // <td>${asignacion.elemento.productoId}</td>
                        // <td>${asignacion.elemento.id}|${asignacion.elemento.numSerie}</td>
                        // <td>${asignacion.usuario.id}|${asignacion.usuario.nombre}</td>
                        // <td>${asignacion.fechaAsignacion}</td>
                        // <c:if test="${asignacion.fechaDevolucion != null}">
                        //     <td>${asignacion.fechaDevolucion}</td>
                        // </c:if>
                        // <c:if test="${asignacion.fechaDevolucion == null}">
                        //     <td>—</td>
                        // </c:if>
                        const asignacionProcesada = {};
                        asignacionProcesada.id = asignacion.id;
                        asignacionProcesada.productoId = asignacion.elemento.productoId;
                        asignacionProcesada.numSerie = asignacion.elemento.id + "|" + asignacion.elemento.numSerie;
                        asignacionProcesada.usuarioNombre = asignacion.usuario.id + "|" + asignacion.usuario.nombre;
                        asignacionProcesada.fechaAsignacion = transformDate(asignacion.fechaAsignacion);
                        asignacionProcesada.fechaDevolucion = "fechaDevolucion" in asignacion ? transformDate(asignacion.fechaDevolucion) : "—";
                        asignaciones.push(asignacionProcesada);
                    });
                    $("#asignacionesTable").bootstrapTable('load', asignaciones);
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
