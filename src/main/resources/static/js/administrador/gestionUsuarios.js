import { eventos } from "/js/administrador/adminUsuarioEventos.js";
import { getToken } from "/js/auth.js";


document.addEventListener("DOMContentLoaded", () => {
    eventos();
});

/* async function obtenerDatosUsuario(documento) {
    const url = `https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/admin/usuario/datos?documento=${encodeURIComponent(documento)}`;

    const response = await fetch(url, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al obtener datos");
    }

    const data = await response.json();

    const campos = [
        { Label: "Nombre", value: data.nombre },
        { Label: "Apellido", value: data.apellido },
        { Label: "Documento", value: data.documento },
        { Label: "Correo", value: data.correo },
        { Label: "Usuario", value: data.username },
        { Label: "Rol", value: data.rol },
        { Label: "Número de cuenta", value: data.numCuenta },
        { Label: "Cuenta", value: data.estado },
        { Label: "Nacimiento", value: data.nacimiento },
        { Label: "Estado", value: data.estadoUsuario },
        { Label: "Bloqueo", value: data.bloqueoUsuario },
    ];

    return campos;
} */

/* async function eliminarUsuario(documento) {
    const url = `https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/admin/usuario/eliminar?documento=${encodeURIComponent(documento)}`;

    const response = await fetch(url, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
            "Content-Type": "application/json"
        }
    })

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    document.getElementById("resultadoUpdate").innerText = "Usuario eliminado.";
    setTimeout(() => {
        window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
    }, 800);
} */

/* async function actualizarEstadoUsuario(datos) {
    const url = "https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/admin/usuario/estado";

    const response = await fetch(url, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datos)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    document.getElementById("resultadoEstado").innerText = "Estado del usuario modificado.";
    setTimeout(() => {
        window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
    }, 800);
} */

/* async function actualizarDatosUsuario(datos) {
    const url = "https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/admin/usuario/actualizar";

    const response = await fetch(url, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datos)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    document.getElementById("resultadoUpdate").innerText = "Datos actualizados correctamente.";
    setTimeout(() => {
        window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
    }, 800);
} */

/* async function crearUsuario(datos) {
    const url = "https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/admin/usuario/crear";

    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datos)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    document.getElementById("resultadoPost").innerText = "Usuario creado correctamente.";
    setTimeout(() => {
        window.location.href = "/html/administrador/usuarios/gestionUsuarios.html";
    }, 800);
} */

/* function llenarTabla(campos) {
    const registroId = document.getElementById("registros");
    if (!registroId) return;

    registroId.innerHTML = "";
    campos.forEach(campo => {
        const td = document.createElement("td");
        td.innerHTML = `<span class="value">${campo.value}</span>`;
        registroId.appendChild(td);
    });

    document.getElementById("resultado").innerText = "Datos cargados correctamente.";
} */

/* function validarDocumento(identidad) {
    const resultado = document.getElementById("resultado");

    if (!identidad || identidad.trim() === "") {
        resultado.innerText = "No ha ingresado el documento.";
        identidad.value = "";
        return;
    }
} */

/* const mapIds = {
    "Nombre": "nombreUpdate",
    "Apellido": "apellidoUpdate",
    "Documento": "documentoUpdate",
    "Correo": "emailUpdate",
    "Usuario": "usernameUpdate",
    "Rol": "rolUpdate",
    "Número de cuenta": "cuentaUpdate",
    "Estado": "estadoUpdate",
    "Nacimiento": "nacimientoUpdate",
    "Contraseña": "passwordUpdate",
    "Estado": "estadoUsuario",
    "Bloqueo": "bloqueoUsuario"
};

function llenarFormulario(campos) {
    campos.forEach(campo => {
        const idInput = mapIds[campo.Label];
        if (idInput) {
            document.getElementById(idInput).value = campo.value;
        }
    });
} */

/* document.getElementById("consultar")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const identidad = document.getElementById("identidad").value;

    if (validarDocumento(identidad)) {
        return
    }

    try {
        const campos = await obtenerDatosUsuario(identidad);
        llenarTabla(campos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
});

document.getElementById("link-trigger-update").addEventListener("click", async () => {
    const identidad = document.getElementById("identidad").value;

    if (validarDocumento(identidad)) {
        return
    }

    try {
        const campos = await obtenerDatosUsuario(identidad);
        llenarFormulario(campos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
}); */

/* document.getElementById("link-trigger-state").addEventListener("click", async () => {
    const identidad = document.getElementById("identidad").value;

    if (validarDocumento(identidad)) {
        return
    }

    try {
        const campos = await obtenerDatosUsuario(identidad);
        llenarFormulario(campos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
});
 */

document.getElementById("actualizarEstadoUsuario").addEventListener("submit", function (e) {
    e.preventDefault();

    const identidad = document.getElementById("identidad").value;
    console.log(identidad);

    if (validarDocumento(identidad)) {
        return
    }

    const datos = {
        documento: identidad,
        estado: document.getElementById("estadoUsuario").value,
        bloqueo: document.getElementById("bloqueoUsuario").value
    };

    try {
        actualizarEstadoUsuario(datos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
})


document.getElementById("actualizarDatosUsuario").addEventListener("submit", function (e) {
    e.preventDefault();

    const identidad = document.getElementById("identidad").value;

    if (validarDocumento(identidad)) {
        return
    }

    const datos = {
        nombre: document.getElementById("nombreUpdate").value,
        apellido: document.getElementById("apellidoUpdate").value,
        documentoActual: identidad,
        documentoNuevo: document.getElementById("documentoUpdate").value,
        email: document.getElementById("emailUpdate").value,
        username: document.getElementById("usernameUpdate").value,
        rol: document.getElementById("rolUpdate").value,
        numCuenta: document.getElementById("cuentaUpdate").value,
        estado: document.getElementById("estadoUpdate").value,
        nacimiento: document.getElementById("nacimientoUpdate").value
    };

    try {
        actualizarDatosUsuario(datos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
})

document.getElementById("confirmarEliminar").addEventListener("click", async () => {
    const identidad = document.getElementById("identidad").value;

    if (validarDocumento(identidad)) {
        return
    }

    try {
        eliminarUsuario(identidad);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
})

document.getElementById("crearUsuario").addEventListener("submit", function (e) {
    e.preventDefault();

    const datos = {
        nombre: document.getElementById("nombrePost").value,
        apellido: document.getElementById("apellidoPost").value,
        documento: document.getElementById("documentoPost").value,
        correo: document.getElementById("emailPost").value,
        username: document.getElementById("usernamePost").value,
        rol: document.getElementById("rolPost").value,
        nacimiento: document.getElementById("nacimientoPost").value,
        password: document.getElementById("passwordPost").value,
        permisos: document.getElementById("permisoPost").value
    };

    console.log(datos);

    try {
        crearUsuario(datos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
})
