/**
 * eSports Arena Manager - Logica Formulario de Inscripcion a Torneo (inscripcion.html)
 * Formulario principal del dominio con validaciones de negocio.
 */

document.addEventListener("DOMContentLoaded", () => {
    inicializarFormularioInscripcion();
});

function inicializarFormularioInscripcion() {
    const selTorneo = document.getElementById("insc-torneo");
    const selTipo = document.getElementById("insc-tipo-participante");
    const form = document.getElementById("formulario-inscripcion");

    cargarTorneosDisponibles();
    cargarParticipantesSegunTipo();

    if (selTorneo) {
        selTorneo.addEventListener("change", () => {
            actualizarResumenTorneo();
            validarFormularioCompleto();
        });
    }

    if (selTipo) {
        selTipo.addEventListener("change", () => {
            cargarParticipantesSegunTipo();
            validarFormularioCompleto();
        });
    }

    const selParticipante = document.getElementById("insc-participante");
    if (selParticipante) {
        selParticipante.addEventListener("change", () => {
            validarFormularioCompleto();
        });
    }

    const emailInput = document.getElementById("insc-email");
    const terminosInput = document.getElementById("insc-terminos");

    if (emailInput) {
        emailInput.addEventListener("input", () => validarEmail(emailInput));
        emailInput.addEventListener("blur", () => validarEmail(emailInput));
    }
    if (terminosInput) {
        terminosInput.addEventListener("change", () => validarTerminos(terminosInput));
    }

    if (form) {
        form.addEventListener("submit", manejarEnvioInscripcion);
    }

    const params = new URLSearchParams(window.location.search);
    const torneoParam = params.get("torneoId");
    if (torneoParam && selTorneo) {
        selTorneo.value = torneoParam;
    }

    actualizarResumenTorneo();
}

function cargarTorneosDisponibles() {
    const selTorneo = document.getElementById("insc-torneo");
    if (!selTorneo) return;

    const torneos = ArenaStorage.getTorneos();
    selTorneo.innerHTML = `<option value="">-- Selecciona un Torneo --</option>`;

    torneos.forEach(t => {
        const opt = document.createElement("option");
        opt.value = t.id;
        opt.textContent = `${t.nombre} (${t.juegoNombre} - ${t.modalidad}) [${t.estado}]`;
        selTorneo.appendChild(opt);
    });
}

function cargarParticipantesSegunTipo() {
    const tipo = document.getElementById("insc-tipo-participante")?.value || "EQUIPO";
    const selParticipante = document.getElementById("insc-participante");
    const labelParticipante = document.getElementById("label-participante");
    const ayudaParticipante = document.getElementById("ayuda-participante");
    if (!selParticipante) return;

    selParticipante.innerHTML = `<option value="">-- Selecciona --</option>`;

    if (tipo === "EQUIPO") {
        if (labelParticipante) labelParticipante.innerHTML = `Selecciona tu Equipo: <span class="requerido">*</span>`;
        if (ayudaParticipante) ayudaParticipante.textContent = `Se listan las escuadras activas registradas en la plataforma.`;

        const equipos = ArenaStorage.getEquipos();
        equipos.forEach(eq => {
            const opt = document.createElement("option");
            opt.value = eq.id;
            opt.textContent = `${eq.nombre} (${eq.integrantes.length} integrantes)`;
            selParticipante.appendChild(opt);
        });
    } else {
        if (labelParticipante) labelParticipante.innerHTML = `Selecciona Jugador: <span class="requerido">*</span>`;
        if (ayudaParticipante) ayudaParticipante.textContent = `Selecciona tu perfil o el jugador a inscribir.`;

        const usuarios = ArenaStorage.getUsuarios().filter(u => u.rol === "JUGADOR");
        usuarios.forEach(u => {
            const opt = document.createElement("option");
            opt.value = u.id;
            opt.textContent = `${u.apodo} - ${u.nombreCompleto} (${u.rango})`;
            selParticipante.appendChild(opt);
        });
    }
}

function actualizarResumenTorneo() {
    const torneoId = parseInt(document.getElementById("insc-torneo")?.value, 10);
    const cajaResumen = document.getElementById("caja-resumen-torneo");
    if (!cajaResumen) return;

    if (!torneoId) {
        cajaResumen.style.display = "none";
        return;
    }

    const torneo = ArenaStorage.getTorneoById(torneoId);
    if (!torneo) return;

    cajaResumen.style.display = "block";
    const cuposLibres = ArenaStorage.calcularCuposDisponibles(torneo);
    const fueraPlazo = ArenaStorage.esInscripcionFueraDePlazo(torneo.cierreInscripcion);

    document.getElementById("resumen-nombre-torneo").textContent = torneo.nombre;
    document.getElementById("resumen-juego-modalidad").textContent = `${torneo.juegoNombre} (${torneo.modalidad})`;
    document.getElementById("resumen-tipo-exigido").textContent = torneo.tipoParticipante;
    document.getElementById("resumen-cupos-info").textContent = `${torneo.cuposOcupados}/${torneo.cupoMaximo} (${cuposLibres} disponibles)`;
    document.getElementById("resumen-cierre-info").textContent = `${formatearFecha(torneo.cierreInscripcion)} ${fueraPlazo ? '(Fuera de plazo)' : '(Vigente)'}`;

    const selTipo = document.getElementById("insc-tipo-participante");
    if (selTipo && selTipo.value !== torneo.tipoParticipante) {
        selTipo.value = torneo.tipoParticipante;
        cargarParticipantesSegunTipo();
    }
}

/**
 * Validaciones de reglas de negocio en cliente.
 */
function validarFormularioCompleto() {
    let esValido = true;
    const alertaGlobal = document.getElementById("alerta-validacion-global");
    const errores = [];

    const torneoId = parseInt(document.getElementById("insc-torneo")?.value, 10);
    const participanteId = parseInt(document.getElementById("insc-participante")?.value, 10);
    const tipoParticipante = document.getElementById("insc-tipo-participante")?.value;
    const emailInput = document.getElementById("insc-email");
    const terminosInput = document.getElementById("insc-terminos");

    if (!torneoId) {
        marcarInvalido("grupo-campo-torneo", "Debes seleccionar un torneo para inscribirte.");
        esValido = false;
    } else {
        marcarValido("grupo-campo-torneo");
        const torneo = ArenaStorage.getTorneoById(torneoId);

        if (torneo.estado !== "ABIERTO") {
            marcarInvalido("grupo-campo-torneo", `El torneo no admite inscripciones porque se encuentra en estado '${torneo.estado}'.`);
            errores.push(`Torneo en estado '${torneo.estado}'. Solo se admiten inscripciones en torneos ABIERTOS.`);
            esValido = false;
        }

        const fueraPlazo = ArenaStorage.esInscripcionFueraDePlazo(torneo.cierreInscripcion);
        if (fueraPlazo) {
            marcarInvalido("grupo-campo-torneo", `El plazo limite de inscripcion vencio el ${formatearFecha(torneo.cierreInscripcion)}.`);
            errores.push("Inscripcion fuera de plazo: la fecha limite ha expirado.");
            esValido = false;
        }

        const cuposLibres = ArenaStorage.calcularCuposDisponibles(torneo);
        if (cuposLibres <= 0) {
            marcarInvalido("grupo-campo-torneo", `El cupo maximo de ${torneo.cupoMaximo} participantes ya se encuentra completo.`);
            errores.push("Cupo maximo alcanzado. No quedan vacantes disponibles en este torneo.");
            esValido = false;
        }

        if (participanteId) {
            const yaInscrito = torneo.inscritos.some(ins => ins.participanteId === participanteId);
            if (yaInscrito) {
                marcarInvalido("grupo-campo-participante", "Este participante ya se encuentra inscrito en este torneo.");
                errores.push("Participante duplicado: ya figura registrado en la lista oficial.");
                esValido = false;
            } else {
                marcarValido("grupo-campo-participante");
            }

            if (tipoParticipante === "EQUIPO") {
                const equipos = ArenaStorage.getEquipos();
                const equipo = equipos.find(e => e.id === participanteId);

                if (equipo) {
                    const juego = ArenaStorage.getJuegos().find(j => j.id === torneo.juegoId);
                    const minExigido = juego ? juego.minIntegrantes : 1;

                    if (equipo.integrantes.length < minExigido) {
                        marcarInvalido("grupo-campo-participante", `El equipo '${equipo.nombre}' tiene ${equipo.integrantes.length} miembros. ${torneo.juegoNombre} exige minimo ${minExigido} integrantes.`);
                        errores.push(`Equipo incompleto: ${equipo.nombre} no alcanza el minimo de ${minExigido} jugadores para ${torneo.juegoNombre}.`);
                        esValido = false;
                    }

                    for (const integrante of equipo.integrantes) {
                        const sancion = ArenaStorage.tieneSancionActiva(integrante.usuarioId);
                        if (sancion.activa) {
                            marcarInvalido("grupo-campo-participante", `El miembro '${integrante.apodo}' tiene una sancion disciplinaria vigente hasta ${formatearFecha(sancion.fechaFin)}. Motivo: ${sancion.motivo}`);
                            errores.push(`Sancion vigente: El jugador '${integrante.apodo}' esta inhabilitado hasta ${formatearFecha(sancion.fechaFin)}.`);
                            esValido = false;
                            break;
                        }
                    }
                }
            } else {
                const sancion = ArenaStorage.tieneSancionActiva(participanteId);
                if (sancion.activa) {
                    marcarInvalido("grupo-campo-participante", `El jugador tiene una sancion vigente hasta ${formatearFecha(sancion.fechaFin)}. Motivo: ${sancion.motivo}`);
                    errores.push(`Jugador sancionado: Inhabilitado para competir hasta ${formatearFecha(sancion.fechaFin)}.`);
                    esValido = false;
                }
            }
        } else {
            marcarInvalido("grupo-campo-participante", "Debes seleccionar un participante.");
            esValido = false;
        }
    }

    if (!validarEmail(emailInput)) {
        esValido = false;
    }

    if (!validarTerminos(terminosInput)) {
        esValido = false;
    }

    const btnSubmit = document.getElementById("btn-submit-inscripcion");
    if (!esValido && errores.length > 0) {
        alertaGlobal.style.display = "flex";
        alertaGlobal.className = "alerta alerta-error";
        alertaGlobal.innerHTML = `
            <div>
                <strong>La inscripcion no puede ser procesada por los siguientes motivos:</strong>
                <ul style="margin-top: 0.5rem; padding-left: 1.25rem;">
                    ${errores.map(e => `<li>${e}</li>`).join("")}
                </ul>
            </div>
        `;
        if (btnSubmit) btnSubmit.disabled = true;
    } else if (esValido) {
        alertaGlobal.style.display = "none";
        if (btnSubmit) btnSubmit.disabled = false;
    }

    return esValido;
}

function validarEmail(input) {
    if (!input) return false;
    const valor = input.value.trim();
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!valor) {
        marcarInvalido("grupo-campo-email", "El correo de contacto es obligatorio.");
        return false;
    } else if (!regexEmail.test(valor)) {
        marcarInvalido("grupo-campo-email", "Ingresa un formato de correo electronico valido (ej: capitan@dominio.cl).");
        return false;
    } else {
        marcarValido("grupo-campo-email");
        return true;
    }
}

function validarTerminos(input) {
    if (!input) return false;
    if (!input.checked) {
        marcarInvalido("grupo-campo-terminos", "Debes aceptar el reglamento de Fair Play para inscribirte.");
        return false;
    } else {
        marcarValido("grupo-campo-terminos");
        return true;
    }
}

function marcarInvalido(grupoId, mensaje) {
    const grupo = document.getElementById(grupoId);
    if (!grupo) return;
    grupo.classList.add("invalido");
    grupo.classList.remove("valido");
    const errorSpan = grupo.querySelector(".mensaje-error");
    if (errorSpan) {
        errorSpan.textContent = mensaje;
        errorSpan.style.display = "flex";
    }
}

function marcarValido(grupoId) {
    const grupo = document.getElementById(grupoId);
    if (!grupo) return;
    grupo.classList.remove("invalido");
    grupo.classList.add("valido");
    const errorSpan = grupo.querySelector(".mensaje-error");
    if (errorSpan) {
        errorSpan.textContent = "";
        errorSpan.style.display = "none";
    }
}

function manejarEnvioInscripcion(e) {
    e.preventDefault();

    if (!validarFormularioCompleto()) {
        mostrarToast("Corrige los errores antes de enviar el formulario.", "error");
        return;
    }

    const torneoId = parseInt(document.getElementById("insc-torneo").value, 10);
    const tipo = document.getElementById("insc-tipo-participante").value;
    const participanteId = parseInt(document.getElementById("insc-participante").value, 10);
    const emailContacto = document.getElementById("insc-email").value.trim();
    const discordUser = document.getElementById("insc-discord").value.trim();

    const torneos = ArenaStorage.getTorneos();
    const indexTorneo = torneos.findIndex(t => t.id === torneoId);
    if (indexTorneo === -1) return;

    let nombreParticipante = "";
    if (tipo === "EQUIPO") {
        const eq = ArenaStorage.getEquipos().find(e => e.id === participanteId);
        nombreParticipante = eq ? eq.nombre : `Equipo #${participanteId}`;
    } else {
        const u = ArenaStorage.getUsuarios().find(u => u.id === participanteId);
        nombreParticipante = u ? u.apodo : `Jugador #${participanteId}`;
    }

    const nuevaInscripcion = {
        id: Date.now(),
        participanteId: participanteId,
        nombre: nombreParticipante,
        tipo: tipo,
        email: emailContacto,
        discord: discordUser,
        fecha: new Date().toISOString().split("T")[0]
    };

    torneos[indexTorneo].inscritos.push(nuevaInscripcion);
    torneos[indexTorneo].cuposOcupados = (torneos[indexTorneo].cuposOcupados || 0) + 1;

    if (!torneos[indexTorneo].ranking.some(r => r.participante === nombreParticipante)) {
        torneos[indexTorneo].ranking.push({
            posicion: torneos[indexTorneo].ranking.length + 1,
            participante: nombreParticipante,
            partidasJugadas: 0,
            victorias: 0,
            derrotas: 0,
            puntos: 0,
            difPuntos: 0
        });
    }

    ArenaStorage.saveTorneos(torneos);

    const alertaGlobal = document.getElementById("alerta-validacion-global");
    alertaGlobal.style.display = "flex";
    alertaGlobal.className = "alerta alerta-exito";
    alertaGlobal.innerHTML = `
        <div>
            <strong>Inscripcion Registrada Exitosamente</strong>
            <p style="margin-top: 0.35rem;">El participante <strong>${nombreParticipante}</strong> ha quedado registrado en <strong>${torneos[indexTorneo].nombre}</strong>.</p>
            <p style="font-size: 0.85rem; margin-top: 0.5rem;">Se ha enviado un correo de confirmacion a: <em>${emailContacto}</em>.</p>
            <div style="margin-top: 1rem;">
                <a href="detalle-torneo.html?id=${torneoId}" class="boton boton-primario boton-sm">Ver Detalle del Torneo</a>
            </div>
        </div>
    `;

    document.getElementById("formulario-inscripcion").reset();
    actualizarResumenTorneo();
    mostrarToast("Inscripcion confirmada con exito", "exito");
}
