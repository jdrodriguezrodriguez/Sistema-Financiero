document.getElementById("ActualizarPersona").addEventListener("submit", function(e){
    e.preventDefault();

    const datosActualizar = {
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        correo: document.getElementById("correo").value,
        nacimiento: document.getElementById("nacimiento").value
    };


    fetch("/actualizarPersona",{
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(datosActualizar)
    })

    .then(response =>{
        if(!response.ok) throw response

        return response.json();
    })

    .then(data =>{
        document.getElementById("resultado").innerText = data.Mensaje
    })

    .catch(async error =>{
        let errData = await error.json();
        document.getElementById("resultado").innerText = "Actualizacion fallida. " + JSON.stringify(errData);
    })

})