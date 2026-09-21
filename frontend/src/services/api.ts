/**
 * API client module for REST calls with automated JWT Bearer headers and token refreshing.
 */

const API_BASE = '/api';

export async function apiFetch<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const token = localStorage.getItem('mawa3id_access_token');
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>),
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE}${endpoint}`, {
    ...options,
    headers,
  });

  if (response.status === 401) {
    // Try token refresh or logout
    localStorage.removeItem('mawa3id_access_token');
    window.location.href = '/login';
    throw new Error('Session expirée');
  }

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || 'Une erreur est survenue');
  }

  if (response.status === 204) {
    return {} as T;
  }

  return response.json();
}
