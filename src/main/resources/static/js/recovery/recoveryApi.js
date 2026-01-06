export async function apiCorreoPassword(url, body) {
    const response = await fetch(url,{
        method: "POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if(!response.ok){
        const ErrorResponse = await response.json();
        throw Error(ErrorResponse.error);
    }
    return response.json();
}

export async function apiCorreoUsername(url, body) {
    const response = await fetch(url,{
        method: "POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if(!response.ok){
        const ErrorResponse = await response.json();
        throw Error(ErrorResponse.error);
    }
    return response.json();
}

export async function apiResetPassword(url, body) {
    const response = await fetch(url,{
        method: "POST",
        headers:{
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if(!response.ok){
        const ErrorResponse = await response.json();
        throw Error(ErrorResponse.error)
    }
    return response.json();
}