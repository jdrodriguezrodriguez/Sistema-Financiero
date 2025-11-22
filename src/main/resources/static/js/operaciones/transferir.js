import { getToken } from "../auth.js";

document.addEventListener("DOMContentLoaded", () => {

    try {
        transferir();
    } catch (error) {
        console.error("Error al cargar el proceso", error);
    }
})

function transferir() {
    const transaccion = document.getElementById("transferirDinero");

    if (transaccion) {

        transaccion.addEventListener("submit", function (e) {
            e.preventDefault();

            const datos = {
                valor: document.getElementById("valor").value,
                cuentaDestino: document.getElementById("cuentaDestino").value,
                descripcion: document.getElementById("descripcion").value
            }
            
            fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/transaccion/transferir", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${getToken()}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(datos)
            })
                .then(response => {
                    if (!response.ok) throw response
                    return response.json();
                })

                .then(data => {
                    document.getElementById("resultado").innerText = data.Mensaje

                    setTimeout(() => {
                        window.location.href = "/html/vistas/index.html";
                    }, 800);
                })

                .catch(async error => {
                    let errorData = await error.json()
                    document.getElementById("resultado").innerText = errorData.detalle;
                })
        })
    }
}