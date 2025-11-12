import { getToken } from "../auth.js";

document.addEventListener("DOMContentLoaded", () => {

    try {
        DepositarDinero();
    } catch (error) {
        console.error("Error al cargar la página segura:", error);
    }

});

function DepositarDinero() {

    const depositarForm = document.getElementById("depositarForm");

    if (depositarForm) {

        depositarForm.addEventListener("submit", function (e) {
            e.preventDefault();

            const valorDeposito = parseFloat(document.getElementById("valor").value);

            fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/transaccion/depositar", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${getToken()}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(valorDeposito)
            })

                .then(response => {
                    if (!response.ok) throw response
                    return response.json();
                })

                .then(data => {
                    document.getElementById("resultadoUsuario").innerText = data.Mensaje

                    setTimeout(() => {
                        window.location.href = "/html/index.html";
                    }, 800);
                })
                .catch(async error => {
                    let errorData = await error.json()
                    document.getElementById("resultadoUsuario").innerText = errorData.detalle;
                })
        })
    }
}
