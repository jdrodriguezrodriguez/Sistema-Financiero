const token = localStorage.getItem("token");

if (!token || token.trim() === "") {
    console.log("⚠️ No hay token en el localStorage");
    window.location.replace("/login.html");
} else {
    console.log("USUARIO AUTENTICADO: ", token);
}


fetch("/api/sistema/personas/4",{
    method: "GET",
    headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    }
})

.then(r => r.json())
.then(data => console.log("Datos:", data))
.catch(err => alert("Error: " + err));


