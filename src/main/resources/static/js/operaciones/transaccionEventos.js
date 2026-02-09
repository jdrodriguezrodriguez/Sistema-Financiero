import { consultarSaldo, depositarDinero, consultarHistorial, transferirDinero } from "/js/operaciones/transaccionService.js"

export async function transaccionEventos() {

    //CONSULTAR SALDO
    const listSaldo = document.querySelector("#listaMonto");

    if (listSaldo) {
        try {
            const data = await consultarSaldo();

            const item = document.createElement("h1");

            item.textContent = "$" + data.saldo;
            listSaldo.appendChild(item)

        } catch (error) {
            document.getElementById("resultadoUsuario").innerText = error.message
        }
    }



    //DEPOSITAR DINERO
    const depositarForm = document.getElementById("depositarForm");

    if (depositarForm) {
        depositarForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            try {
                const valorDeposito = parseFloat(document.getElementById("valor").value);
                console.log(valorDeposito);

                const data = await depositarDinero(valorDeposito);

                document.getElementById("resultadoUsuario").innerText = data.Mensaje

                setTimeout(() => {
                    window.location.href = "/html/vistas/index.html";
                }, 800);

            } catch (error) {
                document.getElementById("resultadoUsuario").innerText = error.message;
            }
        });
    }


    //HISTORIAL
    const listHistorial = document.querySelector("#divLista");

    if (listHistorial) {
        try {
            const historial = await consultarHistorial();
            listHistorial.innerHTML = "";

            historial.forEach(transaccion => {
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
                listHistorial.appendChild(item);
            });
        } catch (error) {
            document.getElementById("resultadoUsuario").innerText = error.message;
        }
    }


    //TRANSFERIR
    const transaccionForm = document.getElementById("transferirDinero");

    if (transaccionForm) {

        transaccionForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            try {
                const datosTransferencia = {
                    valor: document.getElementById("valor").value,
                    cuentaDestino: document.getElementById("cuentaDestino").value,
                    descripcion: document.getElementById("descripcion").value
                }

                const data = await transferirDinero(datosTransferencia);

                document.getElementById("resultado").innerText = data.Mensaje

                setTimeout(() => {
                    window.location.href = "/html/vistas/index.html";
                }, 800);

            } catch (error) {
                document.getElementById("resultado").innerText = error.message;
            }
        });
    }
}