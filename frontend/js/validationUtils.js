/**
 * Utilidades de Validación para Formularios del caso eSports Arena Manager
 */

export function validarApodo(apodo) {
  if (!apodo || apodo.trim() === '') {
    return { valido: false, mensaje: 'El apodo es obligatorio.' };
  }
  if (/\s/.test(apodo)) {
    return { valido: false, mensaje: 'El apodo no admite espacios en blanco.' };
  }
  if (apodo.length < 3 || apodo.length > 20) {
    return { valido: false, mensaje: 'El apodo debe tener entre 3 y 20 caracteres.' };
  }
  return { valido: true, mensaje: '' };
}

export function validarEmail(email) {
  if (!email || email.trim() === '') {
    return { valido: false, mensaje: 'El correo electrónico es obligatorio.' };
  }
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!regex.test(email.trim())) {
    return { valido: false, mensaje: 'Ingrese un formato de correo válido (ej: usuario@arena.com).' };
  }
  return { valido: true, mensaje: '' };
}

export function validarPassword(password) {
  if (!password || password.length < 6) {
    return { valido: false, mensaje: 'La contraseña debe tener al menos 6 caracteres.' };
  }
  return { valido: true, mensaje: '' };
}

export function validarRangoFechas(fechaInicio, fechaFin) {
  if (!fechaInicio || !fechaFin) {
    return { valido: false, mensaje: 'Ambas fechas son obligatorias.' };
  }
  const inicio = new Date(fechaInicio);
  const fin = new Date(fechaFin);
  if (inicio.getTime() > fin.getTime()) {
    return { valido: false, mensaje: 'La fecha de inicio no puede ser posterior a la fecha final.' };
  }
  return { valido: true, mensaje: '' };
}

export function validarPuntaje(puntaje) {
  const num = Number(puntaje);
  if (isNaN(num) || num < 0) {
    return { valido: false, mensaje: 'El puntaje debe ser un número entero mayor o igual a 0.' };
  }
  return { valido: true, mensaje: '' };
}

export function validarNombreEquipo(nombre, equiposExistentes = [], equipoIdActual = null) {
  if (!nombre || nombre.trim().length < 3) {
    return { valido: false, mensaje: 'El nombre del equipo es obligatorio (mínimo 3 caracteres).' };
  }
  const duplicado = equiposExistentes.some(eq => 
    eq.nombre.trim().toLowerCase() === nombre.trim().toLowerCase() && eq.id !== equipoIdActual
  );
  if (duplicado) {
    return { valido: false, mensaje: 'Ya existe un equipo registrado con ese nombre.' };
  }
  return { valido: true, mensaje: '' };
}

if (typeof window !== 'undefined') {
  window.ValidationUtils = {
    validarApodo,
    validarEmail,
    validarPassword,
    validarRangoFechas,
    validarPuntaje,
    validarNombreEquipo
  };
}
