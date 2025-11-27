import { getToken } from "/js/auth.js";

async function obtenerDatosUsuario(documento) {
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
        { Label: "Estado", value: data.estado },
        { Label: "Nacimiento", value: data.nacimiento },
    ];

    return campos;
}


function llenarTabla(campos) {
    const registroId = document.getElementById("registros");
    if (!registroId) return;

    registroId.innerHTML = "";
    campos.forEach(campo => {
        const td = document.createElement("td");
        td.innerHTML = `<span class="value">${campo.value}</span>`;
        registroId.appendChild(td);
    });

    document.getElementById("resultado").innerText = "Datos cargados correctamente.";
}

function validarDocumento(identidad) {
    const resultado = document.getElementById("resultado");

    if (!identidad || identidad.trim() === "") {
        resultado.innerText = "No ha ingresado el documento.";
        identidad.value = "";
        return;
    }
}

const mapIds = {
    "Nombre": "nombreUpdate",
    "Apellido": "apellidoUpdate",
    "Documento": "documentoUpdate",
    "Correo": "emailUpdate",
    "Usuario": "usernameUpdate",
    "Rol": "rolUpdate",
    "Número de cuenta": "cuentaUpdate",
    "Estado": "estadoUpdate",
    "Nacimiento": "nacimientoUpdate",
    "Contraseña": "passwordUpdate"
};

function llenarFormulario(campos) {
    campos.forEach(campo => {
        const idInput = mapIds[campo.Label];
        if (idInput) {
            document.getElementById(idInput).value = campo.value;
        }
    });
}

document.getElementById("consultar")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const identidad = document.getElementById("identidad").value;

    if(validarDocumento(identidad)){
        return
    }

    try {
        const campos = await obtenerDatosUsuario(identidad);
        llenarTabla(campos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
});

document.getElementById("link-trigger-update")?.addEventListener("click", async () => {
    const identidad = document.getElementById("identidad").value;

    if(validarDocumento(identidad)){
        return
    }

    try {
        const campos = await obtenerDatosUsuario(identidad);
        llenarFormulario(campos);
    } catch (error) {
        document.getElementById("resultado").innerText = error.message;
    }
});
