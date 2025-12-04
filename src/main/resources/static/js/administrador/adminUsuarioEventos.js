import { mapearUsuario } from "/js/administrador/adminUsuarioUI.js"
import { consultarUsuario, crearUsuario, eliminarUsuario, llenarTabla, llenarFormulario } from "/js/administrador/adminUsuarioService.js"
import { mapearDatosPostUsuario } from "/js/administrador/adminUsuarioUI.js";

export function eventos() {
    document.getElementById("consultar")?.addEventListener("submit", async (e) => {
        e.preventDefault();

        const documento = document.querySelector("#identidad").value.trim();
        const data = await consultarUsuario(documento);
        const campos = mapearUsuario(data);

        llenarTabla(campos);
    });

    document.getElementById("link-trigger-update").addEventListener("click", async () => {
        const documento = document.querySelector("#identidad").value.trim();
        const data = await consultarUsuario(documento);
        const campos = mapearUsuario(data);
        llenarFormulario(campos);

        document.getElementById("resultado").innerText = error.message;

    });

    document.getElementById("link-trigger-state").addEventListener("click", async () => {
        const documento = document.querySelector("#identidad").value.trim();

        try {
            const data = await consultarUsuario(documento);
            const campos = mapearUsuario(data);
            llenarFormulario(campos);
        } catch (error) {
            document.getElementById("resultado").innerText = error.message;
        }
    });

    document.getElementById("crearUsuario").addEventListener("submit", function (e) {
        e.preventDefault();

        const datos = mapearDatosPostUsuario();

        try {
            crearUsuario(datos);
        } catch (error) {
            document.getElementById("resultado").innerText = error.message;
        }
    })

    document.getElementById("confirmarEliminar").addEventListener("click", async () => {
        const documento = document.querySelector("#identidad").value.trim();

        try {
            eliminarUsuario(documento);
        } catch (error) {
            document.getElementById("resultado").innerText = error.message;
        }
    })
}
