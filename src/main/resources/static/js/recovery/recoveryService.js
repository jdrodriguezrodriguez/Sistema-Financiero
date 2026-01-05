import {apiCorreoUsername, apiCorreoPassword} from "/js/recovery/recoveryApi.js";

const url = "https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/usuarios"

export async function validarCorreoUsername(email){
    return apiCorreoUsername(`${url}/forgotUsername`, email);
}

export async function validarCorreoPassword(email){
    return apiCorreoPassword(`${url}/forgotPassword`, email);
}