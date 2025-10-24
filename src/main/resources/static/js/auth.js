export function getToken(){
    return localStorage.getItem("token");
}

export function setToken(token){
    return localStorage.setItem("token", token);
}

export function removeToken(){
    return localStorage.removeItem("token");
}

export function getUserInfo(){
    return JSON.parse(localStorage.getItem("userInfo"));
}

export function saveUserInfo(user) {
  localStorage.setItem("userInfo", JSON.stringify(user));
}

export async function fetchWithAuth(url, options = {}) {
    const token = getToken();
    if(!token) throw new Error("No se encontró ningún token.");

    const response = await fetch(url,{
        ...options,
        headers: {
            ...options.headers,
            Authorization: `Bearer ${token}`,
        },
    });

    if(response.status == 403 || response.status == 401){
        removeToken();
        window.location.href = "/login.html";
    }

    return response;
}

/*OBTENER DATOS DE SESION AUTENTICADA
const token = localStorage.getItem("token");

if (!token || token.trim() === "") {
    console.log("No hay token en el localStorage");
    window.location.replace("/login.html");
} else {
    console.log("USUARIO AUTENTICADO: ", token ,);
}


fetch("/api/sistema/usuarios/profile",{
    method: "GET",
    headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    }
})

.then(response => response.json())
.then(datos => {
    console.log("Datos:", datos)

    const perfil = document.getElementById("sesion");

    if(perfil){
        perfil.textContent = datos.username;
    }
})
.catch(err => alert("Error: " + err));


//CERRAR SESION
const cerrarSesion = document.getElementById("cerrar-sesion");

if (cerrarSesion) {
    addEventListener("click", (e) =>{
        if(localStorage.getItem("token")){
            localStorage.removeItem("token");
            console.log("Sesión cerrada correctamente.")
        }else{
            console.log("No hay sesión activa.");
        }
        window.location.replace("/login.html");
    })
}*/
