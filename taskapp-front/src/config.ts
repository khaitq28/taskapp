export interface AppConfig {
    apiBaseUrl: string;
    backendBaseUrl: string;
    frontendBaseUrl: string;
    googleOAuthUrl: string;
    oauthPopupOrigin: string;
}

let cachedConfig: AppConfig | undefined = undefined;

export async function loadConfig(): Promise<AppConfig> {
    if (cachedConfig) {
        return cachedConfig;
    }
    try {
        const response = await fetch('/config.json');
        if (!response.ok) {
            throw new Error('Failed to load config.json');
        }
        cachedConfig = await response.json();
    } catch (error) {
        console.error('Error loading config:', error);
        cachedConfig = {
            apiBaseUrl: 'http://localhost:8080/taskapp/api/v1',
            backendBaseUrl: 'http://localhost:8080/taskapp',
            frontendBaseUrl: 'http://localhost',
            googleOAuthUrl: 'http://localhost:8080/taskapp/oauth2/authorization/google',
            oauthPopupOrigin: 'http://localhost:8080'
        };
    }
    return cachedConfig!;
}
