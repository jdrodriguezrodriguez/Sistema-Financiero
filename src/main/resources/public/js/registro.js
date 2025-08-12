document.getElementById("RegistrarForm").addEventListener("submit", function(e){
    e.preventDefault();

    const datos = {
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        documento: document.getElementById("documento").value,
        nacimiento: document.getElementById("nacimiento").value,
        correo: document.getElementById("correo").value,
        password: document.getElementById("password").value
    };

    fetch("/registro", {
        method: "POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datos)
    })

    .then(response => {
            if(!response.ok) throw response
            
            return response.json();
        })

    .then(data =>{
        document.getElementById("resultado").innerText = "Registro exitoso."
    })

    .catch(async error =>{
        let errData = await error.json();
        document.getElementById("resultado").innerText = "Registro fallido. " + JSON.stringify(errData);
    });
    
});