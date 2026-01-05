import { validarCorreoUsername, validarCorreoPassword} from "/js/recovery/recoveryService.js";

export async function recoveryEventos() {

    document.getElementById("resetUsername").addEventListener("submit", async (e) => {
        e.preventDefault();

        const userEmail = document.getElementById("email-user").value;

        try {
            const userValidar = await validarCorreoUsername(userEmail);
            document.getElementById("resultadoUser").innerText = userValidar.Mensaje;
        } catch (error) {
            document.getElementById("resultadoUser").innerText = error.message;
        }
    })

    document.getElementById("resetPassword").addEventListener("submit", async (e) =>{
        e.preventDefault();

        const passEmail = document.getElementById("email-pass").value;
        
        try {
            const passValidar = await validarCorreoPassword(passEmail);
            document.getElementById("resultadoPassword").innerText = passValidar.Mensaje;
        } catch (error) {
            document.getElementById("resultadoPassword").innerText = error.message;
        }
    })
}