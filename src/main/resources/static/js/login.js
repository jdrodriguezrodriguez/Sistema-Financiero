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

    .then(data =>{
        localStorage.setItem("token", data.token);
        window.location.replace("/index.html");
    })

    .catch(async error =>{
        let errores = await error.json();
        document.getElementById("resultado").innerText = errores.detalle
    })    
})
/*
document.addEventListener("DOMContentLoaded", () =>{
    const form = document.getElementById("loginForm");

    form.addEventListener("submit", async (e) =>{
        e.preventDefault()

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        

        const response = await fetch("/autenticar", {
            method: "POST",
            headers:{
                "Content-Type": "application/json"
            },
            body: JSON.stringify({username, password})
        });

        if (response.ok){
            const data = await response.json();
            alert('Inicio de sesión exitoso');
            console.log('Token:', data.token);
        }else {
            alert('Credenciales incorrectas');
        }
    })
});*/
