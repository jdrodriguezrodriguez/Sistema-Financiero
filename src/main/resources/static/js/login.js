import { fetchWithAuth, saveUserInfo, getToken } from "./auth.js";

document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault()


    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;



    fetch("/autenticar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    })

        .then(async response => {
            if (!response.ok) {
                const errorData = await response.json();
                const err = new Error(errorData.error);
                err.timestamp = errorData.timestamp
                err.status = errorData.status
                err.path = errorData.path

                throw err;
            }
            return response.json();
        })

        .then(async data => {
            localStorage.setItem("token", data.token);

            const respose = await fetchWithAuth("/api/sistema/usuarios/profile");
            const userData = await respose.json();

            saveUserInfo(userData);

            window.location.replace("/html/vistas/index.html");
        })

        .catch(error => {
            console.log(error.message + " - " + error.timestamp + " - [" + error.path + "] - " + error.status);
            document.getElementById("resultado").innerText = error.message;

            document.getElementById("username").value = "";
            document.getElementById("username").style.background = "#802222af";

            document.getElementById("password").value = "";
            document.getElementById("password").style.background = "#802222af";
        })
})