import { consultarUsuario, crearUsuario, eliminarUsuario, actualizarEstadousuario, actualizarUsuario, llenarTabla, llenarFormulario, validarDocumento } from "/js/administrador/adminUsuarioService.js"
import { mapearDatosPostUsuario, mapearEstadoUsuario, mapearDatosPutUsuario, mapearTablaUsuario, mapearTablaEmpty } from "/js/administrador/adminUsuarioUI.js";

export function adminEventosUsuarios() {
    document.getElementById("consultar")?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const documento = document.querySelector("#identidad");
        
        if (!validarDocumento(documento)) {
            llenarTabla(mapearTablaEmpty());
            document.getElementById("resultado").innerText = "No ha ingresado el documento.";
            return
        }

        const data = await consultarUsuario(documento.value);
        const campos = mapearTablaUsuario(data);

        llenarTabla(campos);
    });

    document.getElementById("actualizarDatosUsuario").addEventListener("submit", function (e) {
        e.preventDefault();

        const documento = document.querySelector("#identidad").value.trim();

        const datos = mapearDatosPutUsuario(documento);

        try {
            actualizarUsuario(datos);

            document.getElementById("resultadoUpdate").innerText = "Datos actualizados correctamente.";
            setTimeout(() => {
                window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
            }, 800);
        } catch (error) {
            document.getElementById("resultado").innerText = error.message;
        }
    })

    document.getElementById("actualizarEstadoUsuario").addEventListener("submit", function (e) {
        e.preventDefault();

        const documento = document.querySelector("#identidad").value.trim();
        const datos = mapearEstadoUsuario(documento);

        actualizarEstadousuario(datos);

        document.getElementById("resultadoEstado").innerText = "Estado del usuario modificado.";
        setTimeout(() => {
            window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
        }, 800);
    })

    document.getElementById("crearUsuario").addEventListener("submit", function (e) {
        e.preventDefault();

        const datos = mapearDatosPostUsuario();

        try {
            crearUsuario(datos);

            document.getElementById("resultadoPost").innerText = "Usuario creado correctamente.";
            setTimeout(() => {
                window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
            }, 800);
        } catch (error) {
            document.getElementById("resultado").innerText = error.message;
        }
    })

    document.getElementById("confirmarEliminar").addEventListener("click", async () => {
        const documento = document.querySelector("#identidad").value.trim();
        eliminarUsuario(documento);

        document.getElementById("resultado").innerText = "Usuario eliminado.";
        setTimeout(() => {
            window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
        }, 800);
    })

    //CARGAR DATOS EN EL FORMULARIO DE ACTUALIZAR
    document.getElementById("link-trigger-update").addEventListener("click", async () => {
        const documento = document.querySelector("#identidad").value.trim();
        const data = await consultarUsuario(documento);
        const campos = mapearTablaUsuario(data);

        llenarFormulario(campos);
    });

    //CARGAR DATOS EN EL FORMULARIO DE ESTADO DE USUARIO
    document.getElementById("link-trigger-state").addEventListener("click", async () => {
        const documento = document.querySelector("#identidad").value.trim();
        const data = await consultarUsuario(documento);
        const campos = mapearTablaUsuario(data);

        llenarFormulario(campos);
    });

}
