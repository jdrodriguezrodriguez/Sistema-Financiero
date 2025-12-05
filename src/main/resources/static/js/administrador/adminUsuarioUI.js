export function mapearTablaUsuario(data) {
    return [
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
}

export function mapearTablaEmpty() {
    return [
        { Label: "Nombre", value: "" },
        { Label: "Apellido", value: "" },
        { Label: "Documento", value: "" },
        { Label: "Correo", value: "" },
        { Label: "Usuario", value: "" },
        { Label: "Rol", value: "" },
        { Label: "Número de cuenta", value: "" },
        { Label: "Cuenta", value: "" },
        { Label: "Nacimiento", value: "" },
        { Label: "Estado", value: "" },
        { Label: "Bloqueo", value: "" },
    ];
}

export function mapearFormularioUpdate() {
    return {
        "Nombre": "nombreUpdate",
        "Apellido": "apellidoUpdate",
        "Documento": "documentoUpdate",
        "Correo": "emailUpdate",
        "Usuario": "usernameUpdate",
        "Rol": "rolUpdate",
        "Número de cuenta": "cuentaUpdate",
        "Cuenta": "estadoUpdate",
        "Nacimiento": "nacimientoUpdate",
        "Contraseña": "passwordUpdate",
        "Estado": "estadoUsuario",
        "Bloqueo": "bloqueoUsuario"
    };
}

export function mapearDatosPutUsuario(identidad) {
    return {
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
}

export function mapearDatosPostUsuario() {
    return {
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
}

export function mapearEstadoUsuario(identidad) {
    return {
        documento: identidad,
        estado: document.getElementById("estadoUsuario").value,
        bloqueo: document.getElementById("bloqueoUsuario").value
    };
}