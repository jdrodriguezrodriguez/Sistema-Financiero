import { getToken } from "/js/auth.js";

document.addEventListener("DOMContentLoaded", () => {
    try {
        cargarDatosUsuario();
    } catch (error) {
        console.error("Error al cargar la página segura:", error);
    }
})

function cargarDatosUsuario() {

    const consultar = document.getElementById("consultar");

    if (consultar) {
        consultar.addEventListener("submit", function (e) {
            e.preventDefault();

            const documento = document.getElementById("documento").value;

            const url = `https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/admin/userdatos?documento=${encodeURIComponent(documento)}`;

            fetch(url, {

                method: "GET",
                headers: {
                    "Authorization": `Bearer ${getToken()}`,
                    "Content-Type": "application/json"
                }
            })

                .then(response => {
                    if (!response.ok) throw response
                    return response.json();
                })
                .then(data => {

                    const registroId = document.getElementById("registros");

                    if (registroId) {

                        const campos = [
                            { Label: "Nombre", value: data.nombre + " " + data.apellido },
                            { Label: "Documento", value: data.documento },
                            { Label: "Correo", value: data.correo },
                            { Label: "Usuario", value: data.username },
                            { Label: "Rol", value: data.rol },
                            { Label: "Número de cuenta", value: data.numCuenta },
                            { Label: "Estado", value: data.estado },
                        ];

                        console.log(campos);

                        registroId.innerHTML = "";

                        campos.forEach(campo => {

                            const td = document.createElement("td");

                            td.innerHTML = `
                                <span class="label">${campo.Label}:</span> 
                                <span class="value">${campo.value}</span>
                            `;

                            registroId.appendChild(td);
                        });
                        document.getElementById("resultado").innerText = "Datos cargados correctamente.";

                    }

                })
                .catch(async error => {
                    try {
                        if (error instanceof Response) {
                            const errorData = await error.json();
                            document.getElementById("resultado").innerText =errorData.detalle;
                        } else {

                            document.getElementById("resultado").innerText = error.message || "Error inesperado";
                        }
                    } catch (e) {
                        document.getElementById("resultado").innerText = "Error procesando la respuesta del servidor";
                    }
                })

        })
    }


}