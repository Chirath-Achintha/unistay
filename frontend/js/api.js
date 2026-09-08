/**
 * UniStay API Client Utility (api.js)
 * Clean wrapper around Fetch API for communicating with Spring Boot REST Endpoints.
 */
const API = (function () {
    const BASE_URL = (typeof window !== 'undefined' && window.location.origin.includes(':8080')) 
        ? '/api' 
        : 'http://localhost:8080/api';

    /**
     * Helper to perform HTTP Requests
     */
    async function request(endpoint, options = {}) {
        const url = endpoint.startsWith('http') ? endpoint : `${BASE_URL}${endpoint}`;

        const defaultHeaders = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };

        const config = {
            ...options,
            headers: {
                ...defaultHeaders,
                ...options.headers
            }
        };

        if (config.body && typeof config.body === 'object') {
            config.body = JSON.stringify(config.body);
        }

        try {
            const response = await fetch(url, config);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || `HTTP Error ${response.status}`);
            }

            return data;
        } catch (error) {
            console.error(`[API Error] ${options.method || 'GET'} ${url}:`, error.message);
            throw error;
        }
    }

    return {
        get: (endpoint, headers = {}) => request(endpoint, { method: 'GET', headers }),
        post: (endpoint, body, headers = {}) => request(endpoint, { method: 'POST', body, headers }),
        put: (endpoint, body, headers = {}) => request(endpoint, { method: 'PUT', body, headers }),
        delete: (endpoint, headers = {}) => request(endpoint, { method: 'DELETE', headers }),
        
        // System Health API Helper
        checkHealth: () => request('/health')
    };
})();
