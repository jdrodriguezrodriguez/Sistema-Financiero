export function mapearUsuario(data) {
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