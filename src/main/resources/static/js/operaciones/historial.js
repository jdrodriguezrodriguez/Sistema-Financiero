import { getToken } from "../auth.js";

document.addEventListener("DOMContentLoaded", () => {

    try {
        consultarHistorial();
    } catch (error) {
        console.error("Error al cargar la página segura:", error);
    }

});

function consultarHistorial(){
    fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/transaccion/historial",{
        headers:{
            "Authorization": `Bearer ${getToken()}`
        }    
    })
    .then(response => response.json())
    .then(data => {
        const lista = document.getElementById("divLista");

        lista.innerHTML = "";

        data.forEach(transaccion => {
            const item = document.createElement("div");
            item.className = "data-historia";

            item.innerHTML = `
                                <p> <b>Mi cuenta:</b> ${transaccion.cuenta.num_cuenta}<p>
                                <p> <b>Cuenta externa:</b> ${transaccion.cuenta_destino}<p>
                                <p> <b>Tipo de movimiento:</b> ${transaccion.tipo}<p>
                                <p> <b>Valor:</b> $${transaccion.monto}<p>
                                <p> <b>Descripcion:</b> ${transaccion.descripcion}<p>
                                <p> <b>Fecha:</b> ${transaccion.fecha}<p>
                            `
            lista.appendChild(item);
        });
    })
}

