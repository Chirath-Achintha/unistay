/**
 * UniStay Admin JavaScript Utilities (admin.js)
 *
 * Provides:
 *  - checkAdminAuth()      : redirect to login if no valid session token
 *  - adminAPI.*            : API wrappers that inject the X-Admin-Token header
 *  - logout()              : clear session and redirect to login
 *  - getAdminEmail()       : read admin email from sessionStorage
 */

const ADMIN_TOKEN_KEY = 'unistay_admin_token';
const ADMIN_EMAIL_KEY = 'unistay_admin_email';

// -------------------------------------------------------
// Auth helpers
// -------------------------------------------------------

/**
 * Reads the stored admin token.
 * Returns null if no token is stored.
 */
function getAdminToken() {
    return sessionStorage.getItem(ADMIN_TOKEN_KEY);
}

/**
 * Returns the stored admin email or a fallback label.
 */
function getAdminEmail() {
    return sessionStorage.getItem(ADMIN_EMAIL_KEY) || 'Admin';
}

/**
 * Redirects to admin login if no session token is present.
 * Call this at the top of every protected admin page.
 */
function checkAdminAuth() {
    if (!getAdminToken()) {
        window.location.href = './login.html';
    }
}

/**
 * Saves the admin session (token + email) into sessionStorage.
 * sessionStorage is cleared automatically when the browser tab is closed.
 */
function saveAdminSession(token, email) {
    sessionStorage.setItem(ADMIN_TOKEN_KEY, token);
    sessionStorage.setItem(ADMIN_EMAIL_KEY, email);
}

/**
 * Clears the admin session from sessionStorage.
 */
function clearAdminSession() {
    sessionStorage.removeItem(ADMIN_TOKEN_KEY);
    sessionStorage.removeItem(ADMIN_EMAIL_KEY);
}

/**
 * Logs the admin out: calls the logout API, clears session, redirects to login.
 */
async function logout() {
    try {
        await adminAPI.post('/api/admin/logout');
    } catch (e) {
        // Ignore errors — always clear session locally
    }
    clearAdminSession();
    window.location.href = './login.html';
}

// -------------------------------------------------------
// Admin API wrapper (injects X-Admin-Token header)
// -------------------------------------------------------

const adminAPI = (function () {
    const BASE_URL = (window.location.origin.includes(':8080'))
        ? ''
        : 'http://localhost:8080';

    async function request(endpoint, options = {}) {
        const token = getAdminToken();
        const headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            ...(token ? { 'X-Admin-Token': token } : {}),
            ...(options.headers || {})
        };

        const config = {
            ...options,
            headers
        };

        if (config.body && typeof config.body === 'object') {
            config.body = JSON.stringify(config.body);
        }

        const response = await fetch(BASE_URL + endpoint, config);
        const data = await response.json();

        if (response.status === 401) {
            // Session expired – redirect to login
            clearAdminSession();
            window.location.href = './login.html';
            throw new Error('Session expired');
        }

        if (!response.ok) {
            throw new Error(data.message || `HTTP ${response.status}`);
        }

        return data;
    }

    return {
        get:    (endpoint) => request(endpoint, { method: 'GET' }),
        post:   (endpoint, body) => request(endpoint, { method: 'POST', body }),
        put:    (endpoint, body) => request(endpoint, { method: 'PUT', body }),
        delete: (endpoint) => request(endpoint, { method: 'DELETE' })
    };
})();
