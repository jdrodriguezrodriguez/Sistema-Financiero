
export async function apiConsultarSaldo(url, token) {
    const response = await fetch(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        const ErrorResponse = await response.json();
        throw Error(ErrorResponse.error);
    }
    return response.json();
}

export async function apiDepositarDinero(url, token, body){
    const response = await fetch(url,{
        method: "POST",
        headers:{
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(body)
    });

    if(!response.ok){
        const ErrorResponse = await response.json();
        throw Error(ErrorResponse.error);
    }
    return response.json();
}

export async function apiConsultarHistorial(url, token) {
    const response = await fetch(url, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        const ErrorResponse = await response.json();
        throw Error(ErrorResponse.error);
    }
    return response.json();
}

export async function apiTransferirDinero(url, token, body){
    const response = await fetch(url,{
        method: "POST",
        headers:{
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(body)
    });

    if(!response.ok){
        const ErrorResponse = await response.json();
        throw Error(ErrorResponse.error);
    }
    return response.json();
}