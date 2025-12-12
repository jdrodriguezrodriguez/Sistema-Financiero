
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