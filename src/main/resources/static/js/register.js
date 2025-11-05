const idRegistrar = document.getElementById("RegistrarForm");

if (idRegistrar) {
    idRegistrar.addEventListener("submit", function (e) {
        e.preventDefault();

        const datos = {
            nombre: document.getElementById("nombre").value,
            apellido: document.getElementById("apellido").value,
            documento: document.getElementById("documento").value,
            nacimiento: document.getElementById("nacimiento").value,
            correo: document.getElementById("correo").value,
            password: document.getElementById("password").value
        };

        fetch("https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/personas/registrar", {
            method: "POST",
            headers: {
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
                    window.location.href = "/html/login.html";
                }, 800);
            })

            .catch(async error => {
                let errData = await error.json();
                document.getElementById("resultado").innerText = errData;
            });
    });
}

