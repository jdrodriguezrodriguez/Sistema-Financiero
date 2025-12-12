import {consultarSaldo} from "/js/operaciones/transaccionService.js"

export async function  transaccionEventos() {

    const data = await consultarSaldo();
    const lista = document.querySelector("#listaMonto");

    if (lista) {
        const item = document.createElement("h1");

        item.textContent = "$" + data.saldo;
        lista.appendChild(item)
    } else {
        console.log("No hay id para mostrar saldo.")
    }
}