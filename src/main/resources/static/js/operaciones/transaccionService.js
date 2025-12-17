import {apiConsultarSaldo, apiDepositarDinero, apiConsultarHistorial, apiTransferirDinero} from "/js/operaciones/transaccionApi.js"
import { getToken } from "../auth.js";

const url = "https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/transaccion"

export async function consultarSaldo(){
    return apiConsultarSaldo(`${url}/saldo`, getToken());
}

export async function depositarDinero(valor) {
    return apiDepositarDinero(`${url}/depositar`, getToken(), valor);
}

export async function consultarHistorial(){
    return apiConsultarHistorial(`${url}/historial`, getToken());
}

export async function transferirDinero(body) {
    return apiTransferirDinero(`${url}/depositar`, getToken(), body);
}