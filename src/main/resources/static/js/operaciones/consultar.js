import { getToken } from "../auth.js";

document.addEventListener("DOMContentLoaded", () => {

    try {
        consultarSaldoActual();
    } catch (error) {
        console.error("Error al cargar la página segura:", error);
    }

});

function consultarSaldoActual() {

    fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/transaccion/saldo", {
        headers: {
            "Authorization": `Bearer ${getToken()}`
        }
    })
        .then(response => {
            if (!response.ok) throw new Error(`Error HTTP: ${response.status}`);
            return response.json();
        })
        .then(data => {

            const lista = document.getElementById("listaMonto");

            if (lista) {
                const item = document.createElement("h1");

                item.textContent = "$" + data.saldo;
                lista.appendChild(item)
            }else{
                console.log("No hay id para mostrar saldo.")
            }
        })
}