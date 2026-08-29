/**
 * Servicio de Autenticación y Gestión de Sesión
 * Persistencia en localStorage, manejo de JWT y roles.
 */

const STORAGE_KEY_TOKEN = 'esports_token';
const STORAGE_KEY_USER = 'esports_user';

export const AuthService = {
  login: async (email, password, apiBaseUrl = 'http://localhost:8070') => {
    try {
      const response = await fetch(`${apiBaseUrl}/api/v1/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });

      if (!response.ok) {
        if (response.status === 401 || response.status === 400) {
          throw new Error('Credenciales inválidas. Verifique su correo o contraseña.');
        } else if (response.status === 403) {
          throw new Error('Su cuenta ha sido desactivada. Comuníquese con el administrador.');
        } else {
          throw new Error(`Error en el servidor al autenticar (${response.status}).`);
        }
      }

      const data = await response.json();
      const token = data.token || data.jwt || 'mock-jwt-token';
      const user = {
        id: data.id || data.usuarioId || 3,
        email: data.email || email,
        nombre: data.nombre || 'Jugador Autenticado',
        apodo: data.apodo || data.nombre || 'ItsTuki',
        rol: data.rol || 'JUGADOR'
      };

      AuthService.saveSession(token, user);
      return { success: true, user, token };
    } catch (err) {
      // Si el backend no responde, simular login según correo si es modo demo
      if (err.name === 'TypeError' || err.message.includes('Failed to fetch')) {
        console.warn('Backend no disponible, ejecutando en modo simulación de prueba');
        let userRole = 'JUGADOR';
        let userName = 'Jugador Demo';
        let userApodo = 'ItsTuki';
        let userId = 3;

        if (email.includes('admin')) {
          userRole = 'ADMINISTRADOR';
          userName = 'Admin Principal';
          userApodo = 'GrandMaster';
          userId = 1;
        } else if (email.includes('organizador')) {
          userRole = 'ORGANIZADOR';
          userName = 'Organizador Oficial';
          userApodo = 'RefOfficial';
          userId = 2;
        }

        const simulatedUser = {
          id: userId,
          email,
          nombre: userName,
          apodo: userApodo,
          rol: userRole
        };
        const simulatedToken = `simulated-jwt-${userRole.toLowerCase()}-${Date.now()}`;
        AuthService.saveSession(simulatedToken, simulatedUser);
        return { success: true, user: simulatedUser, token: simulatedToken, isSimulated: true };
      }
      throw err;
    }
  },

  registro: async (userData, apiBaseUrl = 'http://localhost:8070') => {
    try {
      const response = await fetch(`${apiBaseUrl}/api/v1/auth/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.mensaje || errorData?.message || `Error en registro (${response.status})`);
      }

      return await response.json();
    } catch (err) {
      if (err.name === 'TypeError' || err.message.includes('Failed to fetch')) {
        return {
          id: Date.now(),
          email: userData.email,
          nombre: userData.nombre,
          apodo: userData.apodo,
          rol: userData.rol || 'JUGADOR',
          mensaje: 'Usuario registrado exitosamente (Modo simulado).'
        };
      }
      throw err;
    }
  },

  saveSession: (token, user) => {
    localStorage.setItem(STORAGE_KEY_TOKEN, token);
    localStorage.setItem(STORAGE_KEY_USER, JSON.stringify(user));
    window.dispatchEvent(new Event('auth-changed'));
  },

  getToken: () => {
    return localStorage.getItem(STORAGE_KEY_TOKEN);
  },

  getCurrentUser: () => {
    const userJson = localStorage.getItem(STORAGE_KEY_USER);
    if (!userJson) return null;
    try {
      return JSON.parse(userJson);
    } catch {
      return null;
    }
  },

  isAuthenticated: () => {
    return !!AuthService.getToken();
  },

  hasRole: (roles) => {
    const user = AuthService.getCurrentUser();
    if (!user) return false;
    if (Array.isArray(roles)) {
      return roles.includes(user.rol);
    }
    return user.rol === roles;
  },

  logout: () => {
    localStorage.removeItem(STORAGE_KEY_TOKEN);
    localStorage.removeItem(STORAGE_KEY_USER);
    window.dispatchEvent(new Event('auth-changed'));
  }
};

if (typeof window !== 'undefined') {
  window.AuthService = AuthService;
}
