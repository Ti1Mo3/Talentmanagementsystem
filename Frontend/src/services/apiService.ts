import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api', // Für Produktion per .env, sonst Proxy
  timeout: 10000,
});

export default {
  get(resource: string, params?: any) {
    return api.get(resource, { params });
  },
  post(resource: string, data: any) {
    return api.post(resource, data);
  },
  put(resource: string, data: any) {
    return api.put(resource, data);
  },
  delete(resource: string) {
    return api.delete(resource);
  },
};
