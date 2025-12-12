import {apiConsultarSaldo} from "/js/operaciones/transaccionApi.js"
import { getToken } from "../auth.js";

const url = "https://didactic-succotash-6j6w5vxw664c4pvv-8081.app.github.dev/api/sistema/transaccion"

export async function consultarSaldo(){
    return apiConsultarSaldo(`${url}/saldo`, getToken());
}