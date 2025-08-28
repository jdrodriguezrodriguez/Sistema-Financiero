document.getElementById("LoginForm").addEventListener("submit", function (e){
    e.preventDefault()

    const datos = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    }

    fetch("/login", {
        method: "POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datos)
    })

    .then(response =>{
        if(!response.ok) throw response

        return response.json();
    })

    .then(data =>{
        /*document.getElementById("resultado").innerText = data.Mensaje*/
        window.location.replace("/index.html");
    })

    .catch(async error =>{
        let errores = await error.json();
        document.getElementById("resultado").innerText = errores.detalle
    })    
})