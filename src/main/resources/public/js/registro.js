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
        document.getElementById("resultado").innerText = data.Mensaje
    })

    .catch(async error =>{
        let errData = await error.json();
       if (errData.nombre) {
            document.getElementById("resultado").innerText = errData.nombre;
        }
        if (errData.apellido) {
            document.getElementById("resultado").innerText = errData.apellido;
        }
        if (errData.documento) {
            document.getElementById("resultado").innerText =  errData.documento;
        }
        if (errData.nacimiento) {
            document.getElementById("resultado").innerText = errData.nacimiento;
        }
        if (errData.correo) {
            document.getElementById("resultado").innerText = errData.correo;
        }
        if (errData.password) {
            document.getElementById("resultado").innerText = errData.password;
        }
    });
    
});