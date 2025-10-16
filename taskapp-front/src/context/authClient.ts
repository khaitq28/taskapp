import {UserLogin} from "../data/UserLogin.ts";
import {loadConfig} from "../config.ts";

let accessInMemory: string | null = null;

export function setAccess(token: string | null) {
    accessInMemory = token;
}
export function getAccess() {
    return accessInMemory;
}

export function parseJwt(token: string): any {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/")
        .padEnd(Math.ceil(base64Url.length / 4) * 4, "="); // padding
    const json = atob(base64);
    try {
        return JSON.parse(decodeURIComponent(
            json.split("").map(c => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2)).join("")
        ));
    } catch {
        return JSON.parse(json);
    }
}

export function claimsToUser(claims: any): UserLogin {
    return {
        id: claims.id,
        email: claims.email,
        name: claims.name,
        role: claims.role ?? "USER"
    };
}

export async function refreshAccess(): Promise<string> {

    const config = await loadConfig();
    const backendUrl = config.backendBaseUrl;

    const resp = await fetch(`${backendUrl}/auth/refresh`, {
        method: "POST",
        credentials: "include"
    });
    if (!resp.ok) throw new Error("refresh_failed");
    const { access } = await resp.json();
    setAccess(access);
    return access;
}

export async function apiFetch(input: RequestInfo, init: RequestInit = {}) {
    let access = getAccess();
    if (!access) {
        // cố gắng silent refresh nếu chưa có access
        access = await refreshAccess().catch(() => null as any);
    }
    const tryOnce = async (tok?: string | null) => fetch(input, {
        ...init,
        headers: {
            ...(init.headers || {}),
            ...(tok ? { Authorization: `Bearer ${tok}` } : {})
        },
        credentials: "include"
    });
    let res = await tryOnce(access);
    if (res.status === 401) {
        const newTok = await refreshAccess().catch(() => null as any);
        res = await tryOnce(newTok);
    }
    return res;
}
