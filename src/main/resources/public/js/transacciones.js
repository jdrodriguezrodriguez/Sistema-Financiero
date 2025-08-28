document.getElementById("transferirDinero").addEventListener("submit", function(e){
    e.preventDefault();

    const datos = {
        valor: document.getElementById("valor").value,
        cuentaDestino: document.getElementById("cuentaDestino").value,
        descripcion: document.getElementById("descripcion").value
    }

    fetch("/transaccion/transferir",{
        method: "POST",
        headers: {
             "Content-Type": "application/json"
        },
        body: JSON.stringify(datos)
    })

    .then(response =>{
        if(!response.ok) throw response
        return response.json();
    })

    .then(data =>{
        document.getElementById("resultado").innerText = data.Mensaje
    })

    .catch(async error =>{
        let errorData = await error.json()
        document.getElementById("resultado").innerText = errorData.detalle;
    })
})