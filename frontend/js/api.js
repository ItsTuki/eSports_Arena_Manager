/**
 * Capa Centralizada de Servicios API REST
 * Conecta con el API Gateway de Spring Boot (Puerto 8070) con soporte para JWT,
 * manejo de errores estándar (400, 401, 403, 404, 500) y fallback de datos simulados.
 */

import { AuthService } from './authService.js';
import {
  MOCK_JUEGOS,
  MOCK_TORNEOS,
  MOCK_EQUIPOS,
  MOCK_INSCRIPCIONES,
  MOCK_PARTIDAS,
  MOCK_RANKINGS,
  MOCK_PREMIOS,
  MOCK_SANCIONES,
  MOCK_NOTIFICACIONES,
  MOCK_USUARIOS
} from './mockData.js';

const DEFAULT_GATEWAY_URL = 'http://localhost:8070';

export class ApiClient {
  constructor(baseUrl = DEFAULT_GATEWAY_URL) {
    this.baseUrl = baseUrl;
    this.useMock = false;
  }

  setBaseUrl(url) {
    this.baseUrl = url;
  }

  setUseMock(value) {
    this.useMock = !!value;
  }

  async request(path, options = {}) {
    if (this.useMock) {
      return null;
    }

    const url = `${this.baseUrl}${path}`;
    const headers = {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    };

    const token = AuthService.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    try {
      const response = await fetch(url, {
        ...options,
        headers
      });

      if (!response.ok) {
        let mensaje = `Error HTTP ${response.status}`;
        try {
          const errData = await response.json();
          mensaje = errData.mensaje || errData.message || errData.error || mensaje;
        } catch {
          // ignore json parse error
        }

        switch (response.status) {
          case 400:
            throw new Error(`Solicitud inválida (400): ${mensaje}`);
          case 401:
            AuthService.logout();
            throw new Error(`Sesión expirada o no autorizada (401). Inicie sesión nuevamente.`);
          case 403:
            throw new Error(`Acceso denegado (403): No tiene los permisos suficientes para esta acción.`);
          case 404:
            throw new Error(`Recurso no encontrado (404): ${mensaje}`);
          case 500:
            throw new Error(`Error interno del servidor (500). Intente más tarde.`);
          default:
            throw new Error(mensaje);
        }
      }

      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return await response.json();
      }
      return await response.text();
    } catch (err) {
      console.warn(`[API Client] Fallo petición a ${url}:`, err.message);
      throw err;
    }
  }

  // --- 1. JUEGOS (game-service) ---
  async getJuegos(todos = false) {
    try {
      return await this.request(`/api/v1/juegos${todos ? '?todos=true' : ''}`);
    } catch {
      return todos ? MOCK_JUEGOS : MOCK_JUEGOS.filter(j => j.activo);
    }
  }

  async getJuegoById(id) {
    try {
      return await this.request(`/api/v1/juegos/${id}`);
    } catch {
      return MOCK_JUEGOS.find(j => j.id === Number(id)) || null;
    }
  }

  async crearJuego(data) {
    try {
      return await this.request(`/api/v1/juegos`, {
        method: 'POST',
        body: JSON.stringify(data)
      });
    } catch {
      const nuevo = { id: Date.now(), ...data, activo: true };
      MOCK_JUEGOS.push(nuevo);
      return nuevo;
    }
  }

  async desactivarJuego(id) {
    try {
      return await this.request(`/api/v1/juegos/${id}/desactivar`, { method: 'PATCH' });
    } catch {
      const j = MOCK_JUEGOS.find(x => x.id === Number(id));
      if (j) j.activo = false;
      return j;
    }
  }

  // --- 2. TORNEOS (tournament-service) ---
  async getTorneos(estado = '') {
    try {
      const query = estado ? `?estado=${estado}` : '';
      return await this.request(`/api/v1/torneos${query}`);
    } catch {
      if (estado) {
        return MOCK_TORNEOS.filter(t => t.estado === estado);
      }
      return MOCK_TORNEOS;
    }
  }

  async getTorneoById(id) {
    try {
      return await this.request(`/api/v1/torneos/${id}`);
    } catch {
      return MOCK_TORNEOS.find(t => t.id === Number(id)) || null;
    }
  }

  async crearTorneo(data) {
    try {
      return await this.request(`/api/v1/torneos`, {
        method: 'POST',
        body: JSON.stringify(data)
      });
    } catch {
      const nuevo = { id: Date.now(), cuposOcupados: 0, estado: 'ABIERTO', ...data };
      MOCK_TORNEOS.push(nuevo);
      return nuevo;
    }
  }

  async cambiarEstadoTorneo(id, nuevoEstado) {
    try {
      return await this.request(`/api/v1/torneos/${id}/estado?nuevoEstado=${nuevoEstado}`, {
        method: 'PATCH'
      });
    } catch {
      const t = MOCK_TORNEOS.find(x => x.id === Number(id));
      if (t) t.estado = nuevoEstado;
      return t;
    }
  }

  // --- 3. INSCRIPCIONES (registration-service) ---
  async getInscripcionesPorTorneo(torneoId) {
    try {
      return await this.request(`/api/v1/inscripciones/torneo/${torneoId}`);
    } catch {
      return MOCK_INSCRIPCIONES.filter(i => i.torneoId === Number(torneoId));
    }
  }

  async inscribir(data) {
    try {
      return await this.request(`/api/v1/inscripciones`, {
        method: 'POST',
        body: JSON.stringify(data)
      });
    } catch {
      const nueva = {
        id: Date.now(),
        fechaInscripcion: new Date().toISOString(),
        estado: 'PENDIENTE',
        ...data
      };
      MOCK_INSCRIPCIONES.push(nueva);
      const torneo = MOCK_TORNEOS.find(t => t.id === Number(data.torneoId));
      if (torneo) torneo.cuposOcupados = (torneo.cuposOcupados || 0) + 1;
      return nueva;
    }
  }

  async cambiarEstadoInscripcion(id, estado) {
    try {
      return await this.request(`/api/v1/inscripciones/${id}/estado`, {
        method: 'PATCH',
        body: JSON.stringify({ estado })
      });
    } catch {
      const ins = MOCK_INSCRIPCIONES.find(i => i.id === Number(id));
      if (ins) ins.estado = estado;
      return ins;
    }
  }

  // --- 4. EQUIPOS (team-service) ---
  async getEquipos(estado = 'ACTIVO') {
    try {
      return await this.request(`/api/v1/equipos?estado=${estado}`);
    } catch {
      return MOCK_EQUIPOS.filter(e => e.estado === estado);
    }
  }

  async getEquipoById(id) {
    try {
      return await this.request(`/api/v1/equipos/${id}`);
    } catch {
      return MOCK_EQUIPOS.find(e => e.id === Number(id)) || null;
    }
  }

  async crearEquipo(data) {
    try {
      return await this.request(`/api/v1/equipos`, {
        method: 'POST',
        body: JSON.stringify(data)
      });
    } catch {
      const nuevo = {
        id: Date.now(),
        estado: 'ACTIVO',
        miembros: [{ usuarioId: data.capitanId, apodo: data.capitanNombre || 'Capitán', rol: 'Capitán' }],
        ...data
      };
      MOCK_EQUIPOS.push(nuevo);
      return nuevo;
    }
  }

  async agregarMiembroEquipo(equipoId, miembroData) {
    try {
      return await this.request(`/api/v1/equipos/${equipoId}/miembros`, {
        method: 'POST',
        body: JSON.stringify(miembroData)
      });
    } catch {
      const eq = MOCK_EQUIPOS.find(e => e.id === Number(equipoId));
      if (eq) {
        eq.miembros = eq.miembros || [];
        eq.miembros.push(miembroData);
      }
      return eq;
    }
  }

  // --- 5. PARTIDAS (match-service) ---
  async getPartidas(torneoId = null, ronda = null, estado = null) {
    try {
      const params = new URLSearchParams();
      if (torneoId) params.append('torneoId', torneoId);
      if (ronda) params.append('ronda', ronda);
      if (estado) params.append('estado', estado);
      const query = params.toString() ? `?${params.toString()}` : '';
      return await this.request(`/api/v1/partidas${query}`);
    } catch {
      return MOCK_PARTIDAS.filter(p => {
        if (torneoId && p.torneoId !== Number(torneoId)) return false;
        if (ronda && p.ronda !== Number(ronda)) return false;
        if (estado && p.estado !== estado) return false;
        return true;
      });
    }
  }

  async crearPartida(data) {
    try {
      return await this.request(`/api/v1/partidas`, {
        method: 'POST',
        body: JSON.stringify(data)
      });
    } catch {
      const nueva = { id: Date.now(), estado: 'PROGRAMADA', resultadoValidado: false, ...data };
      MOCK_PARTIDAS.push(nueva);
      return nueva;
    }
  }

  async actualizarPartida(id, data) {
    try {
      return await this.request(`/api/v1/partidas/${id}`, {
        method: 'PATCH',
        body: JSON.stringify(data)
      });
    } catch {
      const idx = MOCK_PARTIDAS.findIndex(p => p.id === Number(id));
      if (idx !== -1) {
        MOCK_PARTIDAS[idx] = { ...MOCK_PARTIDAS[idx], ...data };
        return MOCK_PARTIDAS[idx];
      }
      return null;
    }
  }

  // --- 6. RANKINGS (ranking-service) ---
  async getRankings(torneoId) {
    try {
      return await this.request(`/api/v1/rankings?torneoId=${torneoId}`);
    } catch {
      return MOCK_RANKINGS.filter(r => r.torneoId === Number(torneoId));
    }
  }

  // --- 7. PREMIOS (prize-service) ---
  async getPremios(torneoId) {
    try {
      return await this.request(`/api/v1/premios?torneoId=${torneoId}`);
    } catch {
      return MOCK_PREMIOS.filter(p => p.torneoId === Number(torneoId));
    }
  }

  async crearPremio(data) {
    try {
      return await this.request(`/api/v1/premios`, {
        method: 'POST',
        body: JSON.stringify(data)
      });
    } catch {
      const nuevo = { id: Date.now(), ...data };
      MOCK_PREMIOS.push(nuevo);
      return nuevo;
    }
  }

  // --- 8. SANCIONES (sanction-service) ---
  async getSanciones(usuarioId = null) {
    try {
      const query = usuarioId ? `?usuarioId=${usuarioId}` : '';
      return await this.request(`/api/v1/sanciones${query}`);
    } catch {
      if (usuarioId) {
        return MOCK_SANCIONES.filter(s => s.usuarioId === Number(usuarioId));
      }
      return MOCK_SANCIONES;
    }
  }

  async crearSancion(data) {
    try {
      return await this.request(`/api/v1/sanciones`, {
        method: 'POST',
        body: JSON.stringify(data)
      });
    } catch {
      const nueva = { id: Date.now(), estado: 'ACTIVA', activa: true, ...data };
      MOCK_SANCIONES.push(nueva);
      return nueva;
    }
  }

  // --- 9. USUARIOS (user-service) ---
  async getUsuarios(rol = '') {
    try {
      const query = rol ? `?rol=${rol}` : '';
      return await this.request(`/api/v1/usuarios${query}`);
    } catch {
      if (rol) {
        return MOCK_USUARIOS.filter(u => u.rol === rol);
      }
      return MOCK_USUARIOS;
    }
  }

  async getUsuarioById(id) {
    try {
      return await this.request(`/api/v1/usuarios/${id}`);
    } catch {
      return MOCK_USUARIOS.find(u => u.id === Number(id)) || null;
    }
  }

  // --- 10. NOTIFICACIONES (notification-service) ---
  async getNotificaciones(usuarioId) {
    try {
      return await this.request(`/api/v1/notificaciones?usuarioId=${usuarioId}`);
    } catch {
      return MOCK_NOTIFICACIONES.filter(n => n.usuarioId === Number(usuarioId));
    }
  }
}

export const api = new ApiClient();

if (typeof window !== 'undefined') {
  window.api = api;
}
