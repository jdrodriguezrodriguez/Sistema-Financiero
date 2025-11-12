/*import { fetchWithAuth, saveUserInfo, getToken } from "./auth.js";

document.addEventListener("DOMContentLoaded", async () =>{
    try {
        const respose = await fetchWithAuth("/api/sistema/usuarios/profile");
        const data = await respose.json();

        console.log("Usuario autenticado correctamente.", data);
        console.log(getToken());
        saveUserInfo(data);
    } catch (error) {
        console.log("Error con el usuario autenticado.")
    }
})*/