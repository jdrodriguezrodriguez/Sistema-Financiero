import { fetchWithAuth, saveUserInfo, getToken } from "./auth.js";

document.getElementById("loginForm").addEventListener("submit", function (e){
    e.preventDefault()

    
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    
    
    
    fetch("/autenticar", {
        method: "POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify({username, password})
    })

    .then(response =>{
        if(!response.ok) throw response

        return response.json();
    })

    .then(async data =>{
        localStorage.setItem("token", data.token);

        const respose = await fetchWithAuth("/api/sistema/usuarios/profile");
        const userData = await respose.json();

        saveUserInfo(userData);

        window.location.replace("/html/vistas/index.html");
    })

    .catch(async error =>{
        let errores = await error.json();
        document.getElementById("resultado").innerText = errores.detalle
    })    
})