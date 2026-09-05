/**
 * eSports Arena Manager - Logica Perfil de Jugador (perfil.html)
 */

document.addEventListener("DOMContentLoaded", () => {
    cargarPerfilUsuario();
    configurarFormularioPerfil();
});

function cargarPerfilUsuario() {
    const usuario = ArenaStorage.getUsuarioActual();
    if (!usuario) return;

    document.getElementById("perfil-avatar").src = usuario.avatar || "https://images.unsplash.com/photo-1566492031773-4f4e44671857?auto=format&fit=crop&w=200&q=80";
    document.getElementById("perfil-avatar").alt = `Avatar de ${usuario.apodo}`;
    document.getElementById("perfil-apodo-header").textContent = usuario.apodo;
    document.getElementById("perfil-nombre-header").textContent = usuario.nombreCompleto;
    document.getElementById("perfil-rol-badge").textContent = usuario.rol;
    document.getElementById("perfil-rango").textContent = usuario.rango || "Sin Rango Oficial";

    document.getElementById("input-perfil-apodo").value = usuario.apodo;
    document.getElementById("input-perfil-nombre").value = usuario.nombreCompleto;
    document.getElementById("input-perfil-email").value = usuario.email;

    const victorias = usuario.victorias || 0;
    const derrotas = usuario.derrotas || 0;
    const totalPartidas = victorias + derrotas;
    const winrate = totalPartidas > 0 ? Math.round((victorias / totalPartidas) * 100) : 0;

    document.getElementById("stat-victorias").textContent = victorias;
    document.getElementById("stat-derrotas").textContent = derrotas;
    document.getElementById("stat-winrate").textContent = `${winrate}%`;

    renderizarEquiposDelJugador(usuario.id);
    renderizarHistorialTorneos(usuario.id);
    renderizarSanciones(usuario.sanciones || []);
}

function renderizarEquiposDelJugador(usuarioId) {
    const contenedor = document.getElementById("lista-equipos-jugador");
    if (!contenedor) return;

    const equipos = ArenaStorage.getEquipos();
    const misEquipos = equipos.filter(eq => eq.integrantes.some(m => m.usuarioId === usuarioId));

    contenedor.innerHTML = "";

    if (misEquipos.length === 0) {
        contenedor.innerHTML = `<li style="color: var(--color-texto-muted); padding: 0.5rem 0;">No perteneces a ningun equipo actualmente.</li>`;
        return;
    }

    misEquipos.forEach(eq => {
        const miRol = eq.integrantes.find(m => m.usuarioId === usuarioId)?.rol || "Miembro";
        const li = document.createElement("li");
        li.style.display = "flex";
        li.style.justifyContent = "space-between";
        li.style.alignItems = "center";
        li.style.padding = "0.6rem 0";
        li.style.borderBottom = "1px solid rgba(255, 255, 255, 0.05)";

        li.innerHTML = `
            <div>
                <strong>${eq.nombre}</strong>
                <div style="font-size: 0.8rem; color: var(--color-texto-secundario);">Rol: ${miRol}</div>
            </div>
            <a href="equipos.html" class="boton boton-secundario boton-sm" style="font-size: 0.75rem; padding: 0.2rem 0.5rem;">Ver</a>
        `;
        contenedor.appendChild(li);
    });
}

function renderizarHistorialTorneos(usuarioId) {
    const contenedor = document.getElementById("lista-historial-torneos");
    if (!contenedor) return;

    const torneos = ArenaStorage.getTorneos();
    const equipos = ArenaStorage.getEquipos().filter(eq => eq.integrantes.some(m => m.usuarioId === usuarioId));
    const idsEquipos = equipos.map(e => e.id);

    const torneosParticipados = torneos.filter(t => {
        return t.inscritos.some(ins => {
            if (ins.tipo === "INDIVIDUAL" && ins.participanteId === usuarioId) return true;
            if (ins.tipo === "EQUIPO" && idsEquipos.includes(ins.participanteId)) return true;
            return false;
        });
    });

    contenedor.innerHTML = "";

    if (torneosParticipados.length === 0) {
        contenedor.innerHTML = `<li style="color: var(--color-texto-muted); padding: 0.5rem 0;">No registras participaciones en torneos aun.</li>`;
        return;
    }

    torneosParticipados.forEach(t => {
        const li = document.createElement("li");
        li.style.display = "flex";
        li.style.justifyContent = "space-between";
        li.style.alignItems = "center";
        li.style.padding = "0.75rem 0";
        li.style.borderBottom = "1px solid rgba(255, 255, 255, 0.05)";

        li.innerHTML = `
            <div>
                <strong style="color: #FFFFFF;">${t.nombre}</strong>
                <div style="font-size: 0.85rem; color: var(--color-texto-secundario);">
                    ${t.juegoNombre} &bull; ${formatearFecha(t.fechaInicio)}
                </div>
            </div>
            <div>
                ${generarBadgeEstado(t.estado)}
                <a href="detalle-torneo.html?id=${t.id}" class="boton boton-secundario boton-sm" style="margin-left: 0.5rem; padding: 0.2rem 0.5rem; font-size: 0.75rem;">Detalle</a>
            </div>
        `;
        contenedor.appendChild(li);
    });
}

function renderizarSanciones(sanciones = []) {
    const contenedor = document.getElementById("lista-sanciones-jugador");
    if (!contenedor) return;

    contenedor.innerHTML = "";

    if (sanciones.length === 0) {
        contenedor.innerHTML = `
            <div style="color: var(--color-acento); background: rgba(0, 245, 212, 0.05); padding: 0.75rem; border-radius: var(--radio-borde-sm); font-size: 0.9rem; border: 1px solid var(--color-acento);">
                <strong>Historial Limpio:</strong> Este jugador no posee sanciones disciplinarias activas ni historicas.
            </div>
        `;
        return;
    }

    sanciones.forEach(s => {
        const esVigente = s.estado === "VIGENTE";
        const div = document.createElement("div");
        div.style.padding = "0.75rem";
        div.style.marginBottom = "0.5rem";
        div.style.borderRadius = "var(--radio-borde-sm)";
        div.style.border = `1px solid ${esVigente ? 'var(--color-error)' : 'var(--color-borde)'}`;
        div.style.backgroundColor = esVigente ? 'var(--color-error-fondo)' : 'var(--color-fondo)';

        div.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.35rem;">
                <strong style="color: ${esVigente ? 'var(--color-error)' : 'var(--color-texto-secundario)'};">
                    ${esVigente ? 'Sancion Vigente (Inhabilitado)' : 'Sancion Cumplida'}
                </strong>
                <span class="badge ${esVigente ? 'badge-en-curso' : 'badge-finalizado'}">${s.estado}</span>
            </div>
            <p style="font-size: 0.85rem; margin-bottom: 0.25rem;"><strong>Motivo:</strong> ${s.motivo}</p>
            <div style="font-size: 0.75rem; color: var(--color-texto-muted);">
                Periodo: ${formatearFecha(s.fechaInicio)} al ${formatearFecha(s.fechaFin)}
            </div>
        `;
        contenedor.appendChild(div);
    });
}

function configurarFormularioPerfil() {
    const form = document.getElementById("formulario-editar-perfil");
    const apodoInput = document.getElementById("input-perfil-apodo");
    const emailInput = document.getElementById("input-perfil-email");

    if (apodoInput) {
        apodoInput.addEventListener("input", () => validarApodo(apodoInput));
    }
    if (emailInput) {
        emailInput.addEventListener("input", () => validarEmailPerfil(emailInput));
    }

    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();

            const apodoValido = validarApodo(apodoInput);
            const emailValido = validarEmailPerfil(emailInput);
            const nombreValido = validarNombrePerfil(document.getElementById("input-perfil-nombre"));

            if (!apodoValido || !emailValido || !nombreValido) {
                mostrarToast("Corrige los errores del formulario de perfil.", "error");
                return;
            }

            const usuarioActual = ArenaStorage.getUsuarioActual();
            const nuevoApodo = apodoInput.value.trim();
            const nuevoEmail = emailInput.value.trim();
            const nuevoNombre = document.getElementById("input-perfil-nombre").value.trim();

            usuarioActual.apodo = nuevoApodo;
            usuarioActual.email = nuevoEmail;
            usuarioActual.nombreCompleto = nuevoNombre;

            const usuarios = ArenaStorage.getUsuarios();
            const idx = usuarios.findIndex(u => u.id === usuarioActual.id);
            if (idx !== -1) {
                usuarios[idx] = { ...usuarios[idx], ...usuarioActual };
                ArenaStorage.saveUsuarios(usuarios);
            }

            ArenaStorage.setUsuarioActual(usuarioActual);

            const feedback = document.getElementById("feedback-perfil");
            if (feedback) {
                feedback.style.display = "block";
                feedback.className = "alerta alerta-exito";
                feedback.innerHTML = "Datos del perfil actualizados correctamente.";
            }

            mostrarToast("Perfil guardado con exito", "exito");
            cargarPerfilUsuario();
        });
    }
}

function validarApodo(input) {
    if (!input) return false;
    const valor = input.value.trim();
    const grupo = document.getElementById("grupo-perfil-apodo");

    if (!valor) {
        marcarInvalidoPerfil(grupo, "El apodo es obligatorio.");
        return false;
    }
    if (/\s/.test(input.value)) {
        marcarInvalidoPerfil(grupo, "El apodo no admite espacios en blanco.");
        return false;
    }
    if (valor.length < 3 || valor.length > 20) {
        marcarInvalidoPerfil(grupo, "El apodo debe tener entre 3 y 20 caracteres.");
        return false;
    }

    marcarValidoPerfil(grupo);
    return true;
}

function validarEmailPerfil(input) {
    if (!input) return false;
    const valor = input.value.trim();
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const grupo = document.getElementById("grupo-perfil-email");

    if (!valor) {
        marcarInvalidoPerfil(grupo, "El correo electronico es obligatorio.");
        return false;
    }
    if (!regex.test(valor)) {
        marcarInvalidoPerfil(grupo, "Formato de correo no valido.");
        return false;
    }

    marcarValidoPerfil(grupo);
    return true;
}

function validarNombrePerfil(input) {
    if (!input) return false;
    const valor = input.value.trim();
    const grupo = document.getElementById("grupo-perfil-nombre");

    if (!valor || valor.length < 3) {
        marcarInvalidoPerfil(grupo, "El nombre completo debe tener al menos 3 caracteres.");
        return false;
    }

    marcarValidoPerfil(grupo);
    return true;
}

function marcarInvalidoPerfil(grupo, msg) {
    if (!grupo) return;
    grupo.classList.add("invalido");
    grupo.classList.remove("valido");
    const err = grupo.querySelector(".mensaje-error");
    if (err) {
        err.textContent = msg;
        err.style.display = "flex";
    }
}

function marcarValidoPerfil(grupo) {
    if (!grupo) return;
    grupo.classList.remove("invalido");
    grupo.classList.add("valido");
    const err = grupo.querySelector(".mensaje-error");
    if (err) {
        err.textContent = "";
        err.style.display = "none";
    }
}
