import { mapearFormularioUpdate } from "/js/administrador/adminUsuarioUI.js";
import { apiPutEstadoUsuario } from "/js/administrador/api.js";
import {apiGetUsuario, apiDeleteUsuario, apiPostUsuario, apiPutUsuario} from "/js/administrador/api.js"
import { getToken } from "/js/auth.js";

const url = "https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/admin/usuario"

export function consultarUsuario(documento){
    return apiGetUsuario(`${url}/datos?documento=${encodeURIComponent(documento)}`, getToken());
}

export function actualizarUsuario(body){
    return apiPutUsuario(`${url}/actualizar`, getToken(), body);
}

export function crearUsuario(body){
    return apiPostUsuario(`${url}/crear`, getToken(), body);
}

export function actualizarEstadousuario(body){
    return apiPutEstadoUsuario(`${url}/estado`, getToken(), body);
}

export function eliminarUsuario(documento){
    validarDocumento(documento);
    return apiDeleteUsuario(`${url}/eliminar?documento=${encodeURIComponent(documento)}`, getToken());
}

export function llenarTabla(campos) {
    const registroId = document.getElementById("registros");
    if (!registroId) return;

    registroId.innerHTML = "";
    campos.forEach(campo => {
        const td = document.createElement("td");
        td.innerHTML = `<span class="value">${campo.value}</span>`;
        registroId.appendChild(td);
    });

    return document.getElementById("resultado").innerText = "Datos cargados correctamente.";
}

export function llenarFormulario(campos) {
    const mapIds = mapearFormularioUpdate();

    campos.forEach(campo => {
        const idInput = mapIds[campo.Label];
        if (idInput) {
            document.getElementById(idInput).value = campo.value;
        }
    });
    return
}

export function validarDocumento(identidad) {
    
    const valor = identidad.value.trim();

    if (!valor) {
        identidad.value = "";
        return false;
    }
    return true;
}
