import { getToken, removeToken } from "./auth.js";

document.addEventListener("DOMContentLoaded", () => {

    try {
        actualizarDatosPersona();
    } catch (error) {
        console.error("Error al cargar la página segura:", error);
    }
    
});

function actualizarDatosPersona() {
    const actualizarPersona = document.getElementById("ActualizarPersona");

    if (actualizarPersona) {
        actualizarPersona.addEventListener("submit", function (e) {
            e.preventDefault();

            const datosPer = {
                nombre: document.getElementById("nombre").value,
                apellido: document.getElementById("apellido").value,
                correo: document.getElementById("correo").value,
                nacimiento: document.getElementById("nacimiento").value
            };

            fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/personas/actualizar", {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${getToken()}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(datosPer)
            })

                .then(response => {
                    if (!response.ok) throw response

                    return response.json();
                })

                .then(data => {
                    document.getElementById("resultadoPersona").innerText = data.Mensaje
                    
                    setTimeout(() => {
                        window.location.href = "/html/index.html";
                    }, 800);
                })

                .catch(async error => {
                    let errData = await error.json();
                    document.getElementById("resultadoUsuario").innerText = "Actualizacion fallida. " + JSON.stringify(errData);
                    console.log(JSON.stringify(errData));
                })
        })
    }
}

document.addEventListener("DOMContentLoaded", () => {

    const ActualizarUsuario = document.getElementById("ActualizarUsuario");

    if (ActualizarUsuario) {
        ActualizarUsuario.addEventListener("submit", function (e) {
            e.preventDefault();

            const datosUsuarioActualizar = {
                username: document.getElementById("username").value,
                password: document.getElementById("password").value
            }

            fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/usuarios/actualizar", {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${getToken()}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(datosUsuarioActualizar)
            })

                .then(response => {
                    if (!response.ok) throw response
                    return response.json();
                })

                .then(data => {
                    document.getElementById("resultadoUsuario").innerText = data.Mensaje
                    removeToken();

                    setTimeout(() => {
                        window.location.href = "/html/login.html";
                    }, 800);
                    
                })

                .catch(async error => {
                    let errData = await error.json();
                    document.getElementById("resultadoUsuario").innerText = "Actualizacion fallida. " + JSON.stringify(errData);
                })
        })
    }
});
