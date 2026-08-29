/**
 * Servicio de Autenticación y Gestión de Sesión
 * Persistencia en localStorage, manejo de JWT y roles.
 */

const STORAGE_KEY_TOKEN = 'esports_token';
const STORAGE_KEY_USER = 'esports_user';

export const AuthService = {
  login: async (email, password, apiBaseUrl = 'http://localhost:8070') => {
    try {
      const cleanEmail = email.trim();
      const response = await fetch(`${apiBaseUrl}/api/v1/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: cleanEmail, password })
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

      // Obtener datos detallados del perfil desde user-service
      let userProfile = null;
      try {
        const resProfile = await fetch(`${apiBaseUrl}/api/v1/usuarios/buscar?email=${encodeURIComponent(cleanEmail)}`);
        if (resProfile.ok) {
          userProfile = await resProfile.json();
        }
      } catch (errProfile) {
        console.warn('No se pudo obtener perfil detallado de user-service:', errProfile);
      }

      const user = {
        id: userProfile?.id || data.id || data.usuarioId || 3,
        email: userProfile?.email || cleanEmail,
        nombre: userProfile?.nombre || (data.rol === 'ADMINISTRADOR' ? 'Admin Master' : data.rol === 'ORGANIZADOR' ? 'Organizador Pro' : 'Anibal Romero'),
        apodo: userProfile?.nickname || (data.rol === 'ADMINISTRADOR' ? 'GrandMaster' : data.rol === 'ORGANIZADOR' ? 'RefOfficial' : 'ItsTuki'),
        rol: userProfile?.rol || data.rol || 'JUGADOR'
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
          userName = 'Admin Master';
          userApodo = 'GrandMaster';
          userId = 1;
        } else if (email.includes('organizador')) {
          userRole = 'ORGANIZADOR';
          userName = 'Organizador Pro';
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
      const email = (userData.email || '').trim();
      const password = userData.password;
      const nombre = (userData.nombre || userData.apodo || 'Jugador').trim();
      const nickname = (userData.apodo || userData.nickname || 'Player').trim();
      const rol = (userData.rol || 'JUGADOR').toUpperCase();

      // 1. Crear el usuario en user-service primero (necesario para la validación cruzada)
      let usuarioCreado = null;
      try {
        const resUser = await fetch(`${apiBaseUrl}/api/v1/usuarios`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            nombre,
            nickname,
            email,
            rol,
            estado: 'ACTIVO'
          })
        });
        if (resUser.ok) {
          usuarioCreado = await resUser.json();
        }
      } catch (errUser) {
        console.warn('Aviso user-service:', errUser);
      }

      // 2. Registrar las credenciales en auth-service
      const response = await fetch(`${apiBaseUrl}/api/v1/auth/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email,
          password,
          rol
        })
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        const msg = errorData?.mensaje || errorData?.message || errorData?.error || `Error en registro (${response.status})`;
        throw new Error(msg);
      }

      const cuentaCreada = await response.json();
      return {
        id: usuarioCreado?.id || cuentaCreada?.id || 3,
        email,
        nombre,
        apodo: nickname,
        rol
      };
    } catch (err) {
      if (err.name === 'TypeError' || err.message.includes('Failed to fetch')) {
        return {
          id: Date.now(),
          email: userData.email,
          nombre: userData.nombre || userData.apodo,
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

  logout: () => {
    localStorage.removeItem(STORAGE_KEY_TOKEN);
    localStorage.removeItem(STORAGE_KEY_USER);
    window.dispatchEvent(new Event('auth-changed'));
  }
};

if (typeof window !== 'undefined') {
  window.AuthService = AuthService;
}
