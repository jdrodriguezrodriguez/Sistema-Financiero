
//TOKEN
export function getToken(){
    return localStorage.getItem("token");
}

export function setToken(token){
    return localStorage.setItem("token", token);
}

export function removeToken(){
    return localStorage.removeItem("token");
}

export function statusToken(){
    const token = getToken();
    
    if(!token){
        window.location.replace("../html/login.html");
        return
    }

    try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        const now = Math.floor(Date.now() / 1000);
        
        if (payload.exp && payload.exp < now) {
            console.warn("Token expirado.");
            localStorage.removeItem("token");
            window.location.replace("../html/login.html");
            return;
        }
    } catch (error) {
        console.error("Token inválido:", error);
        localStorage.removeItem("token");
        window.location.replace("../html/login.html");
        return;
    }
}

//USER
export function getUserInfo(){
    return JSON.parse(localStorage.getItem("userInfo"));
}

export function saveUserInfo(user) {
  localStorage.setItem("userInfo", JSON.stringify(user));
}

//FETCH
export async function fetchWithAuth(url, options = {}) {
    const token = getToken();
    if(!token) throw new Error("No se encontró ningún token.");

    const response = await fetch(url,{
        ...options,
        headers: {
            ...options.headers,
            Authorization: `Bearer ${token}`,
        },
    });

    if(response.status == 403 || response.status == 401){
        removeToken();
        window.location.href = "/login.html";
    }

    return response;
}
