export async function apiGetUsuario(url, token) {
    
    const response = await fetch(url, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al obtener datos");
    }

    return response.json();
}

export async function apiPutUsuario(url, token, body) {

    const response = await fetch(url, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    return response.json();
}

export async function apiPostUsuario(url, token, body) {

    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    return response.json();
}

export async function apiPutEstadoUsuario(url, token, body) {

    const response = await fetch(url, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    return response.json();
}

export async function apiDeleteUsuario(url, token) {

    const response = await fetch(url, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detalle || "Error al enviar datos");
    }

    return true;
}