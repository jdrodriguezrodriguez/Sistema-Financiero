import { statusToken, getUserInfo, getToken, removeToken } from "./auth.js";

document.getElementById("cerrar-sesion").addEventListener("click", (e) => {
    e.preventDefault();
    if (getToken()) {
        removeToken();
    } else {
        alert("NO HAY SESIÓN ACTIVA.");
    }
    window.location.replace("../html/login.html");
});


document.addEventListener("DOMContentLoaded", () => {
    try {
        statusToken();
        console.log("Página segura cargada correctamente.");
        
        const user = getUserInfo();
        if (user) {
            document.getElementById("sesion").textContent = user.username;
        } else {
            console.warn("Usuario no autenticado.");
        }

        cargarDatosPerfil();
    } catch (err) {
        console.error("Error al cargar la página segura:", err);
    }
});


function cargarDatosPerfil() {
    fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/usuarios/profile/datos", {
        headers: {
            "Authorization": `Bearer ${getToken()}`
        }

    })
        .then(response => {
            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);
            return response.json();
        })
        .then(data => {

            const lista = document.getElementById("lista");

            if (lista) {
                const campos = [
                    { Label: "Nombre", value: data.nombre + " " + data.apellido },
                    { Label: "Documento", value: data.documento },
                    { Label: "Correo", value: data.correo },
                    { Label: "Usuario", value: data.username },
                    { Label: "Rol", value: data.rol },
                    { Label: "Número de cuenta", value: data.numCuenta },
                    { Label: "Estado", value: data.estado },
                ];

                lista.innerHTML = "";

                campos.forEach(campo => {
                    const item = document.createElement("li");
                    item.innerHTML = `<span class="label">${campo.Label}:</span> <span class="value">${campo.value}</span>`;
                    lista.appendChild(item);
                });
            }else{
                console.log("No hay lista para mostrar datos del usuario.")
            }
        })
        .catch(error => {
            console.error("Error cargando datos:", error);
            document.getElementById("lista").innerHTML = 
                "<li>Error al cargar datos del perfil.</li>";
        });
}

/*
async function apiFetch(endpoint) {
    const res = await fetch(endpoint, {
        headers: { "Authorization": `Bearer ${getToken()}` }
    });
    if (!res.ok) throw new Error(`Error HTTP: ${res.status}`);
    return res.json();
}

*/ 