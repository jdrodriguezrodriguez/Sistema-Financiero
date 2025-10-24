import { getUserInfo } from "./auth";

document.addEventListener("DOMContentLoaded", () =>{
    const user = getUserInfo();

    if (user) {
        document.getElementById("sesion").textContent = user.username;
    } else {
        console.warn("Usuario no autenticado.");
    }
});

/*fetch('')
                .then(response => response.json())
                .then(data => {
                    const lista = document.getElementById("lista");

                    const campos = [
                        { Label: "Nombre", value: data.nombre + " " + data.apellido },
                        { Label: "Documento", value: data.documento },
                        { Label: "Correo", value: data.correo },
                        { Label: "Usuario", value: data.username },
                        { Label: "Rol", value: data.rol },
                        { Label: "Número de cuenta", value: data.numCuenta },
                        { Label: "Estado", value: data.estado },
                    ];

                    campos.forEach(campo => {
                        const item = document.createElement("li");
                        item.innerHTML = `<span class="label">${campo.Label}:</span> <span class="value">${campo.value}</span>`;
                        lista.appendChild(item);
                    });
                });*/


