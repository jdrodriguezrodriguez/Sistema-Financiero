import { validarCorreoUsername, validarCorreoPassword, URLSearch, resetPassword } from "/js/recovery/recoveryService.js";

export async function recoveryEventos() {

    const resetUsername = document.getElementById("resetUsername");

    if (resetUsername) {
        resetUsername.addEventListener("submit", async (e) => {
            e.preventDefault();

            const userEmail = document.getElementById("email-user").value;

            try {
                const userValidar = await validarCorreoUsername(userEmail);
                document.getElementById("resultadoUser").innerText = userValidar.Mensaje;
            } catch (error) {
                document.getElementById("resultadoUser").innerText = error.message;
            }
        })
    }

    const forgotPassword = document.getElementById("resetPassword");

    if (forgotPassword) {
        forgotPassword.addEventListener("submit", async (e) => {
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

    const changePassword = document.getElementById("changePassword");

    if (changePassword) {
        changePassword.addEventListener("submit", async (e) => {
            e.preventDefault();
            
            const body = {
                token: URLSearch(),
                password: document.getElementById("Password").value,
                newPassword: document.getElementById("newPassword").value
            }

            if (body.password != body.newPassword) {
                document.getElementById("resultadoPassword").innerText = "Las contraseñas no coindicen.";
                return;
            }

            try {
                const reset = await resetPassword(body);
                document.getElementById("resultadoPassword").innerText = reset.Mensaje;
                setTimeout(() => {
                    window.location.href = "/html/vistas/login.html";
                }, 1000);
            } catch (error) {
                document.getElementById("resultadoPassword").innerText = error.message;
            }
        })
    }
}