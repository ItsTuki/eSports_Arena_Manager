/**
 * eSports Arena Manager - Logica Gestion de Equipos (equipos.html)
 */

let listaMiembrosFormulario = [];

document.addEventListener("DOMContentLoaded", () => {
    cargarJuegosEnSelect();
    cargarCapitanesDisponibles();
    cargarUsuariosParaAgregar();
    renderizarListaEquipos();
    configurarFormularioEquipo();
});

function cargarJuegosEnSelect() {
    const sel = document.getElementById("equipo-juego");
    if (!sel) return;
    const juegos = ArenaStorage.getJuegos();
    sel.innerHTML = `<option value="">-- Selecciona el juego principal --</option>`;
    juegos.forEach(j => {
        const opt = document.createElement("option");
        opt.value = j.id;
        opt.textContent = `${j.nombre} (${j.modalidad} - Minimo ${j.minIntegrantes} jugadores)`;
        sel.appendChild(opt);
    });
}

function cargarCapitanesDisponibles() {
    const sel = document.getElementById("equipo-capitan");
    if (!sel) return;
    const usuarios = ArenaStorage.getUsuarios().filter(u => u.rol === "JUGADOR");
    sel.innerHTML = `<option value="">-- Selecciona al Capitan --</option>`;
    usuarios.forEach(u => {
        const opt = document.createElement("option");
        opt.value = u.id;
        opt.textContent = `${u.apodo} (${u.nombreCompleto})`;
        sel.appendChild(opt);
    });

    sel.addEventListener("change", (e) => {
        const capId = parseInt(e.target.value, 10);
        if (capId) {
            const user = usuarios.find(u => u.id === capId);
            if (user) {
                const index = listaMiembrosFormulario.findIndex(m => m.usuarioId === user.id);
                if (index !== -1) {
                    listaMiembrosFormulario[index].rol = "Capitan";
                } else {
                    listaMiembrosFormulario.unshift({
                        usuarioId: user.id,
                        apodo: user.apodo,
                        rol: "Capitan"
                    });
                }
                renderizarMiembrosFormulario();
            }
        }
    });
}

function cargarUsuariosParaAgregar() {
    const sel = document.getElementById("miembro-usuario-select");
    if (!sel) return;
    const usuarios = ArenaStorage.getUsuarios().filter(u => u.rol === "JUGADOR");
    sel.innerHTML = `<option value="">-- Seleccionar jugador --</option>`;
    usuarios.forEach(u => {
        const opt = document.createElement("option");
        opt.value = u.id;
        opt.textContent = `${u.apodo} - ${u.nombreCompleto}`;
        sel.appendChild(opt);
    });
}

function configurarFormularioEquipo() {
    const btnAgregarMiembro = document.getElementById("btn-agregar-miembro");
    const form = document.getElementById("formulario-crear-equipo");

    if (btnAgregarMiembro) {
        btnAgregarMiembro.addEventListener("click", agregarMiembroALista);
    }

    if (form) {
        form.addEventListener("submit", manejarCreacionEquipo);
    }

    const usuarioActual = ArenaStorage.getUsuarioActual();
    if (usuarioActual && usuarioActual.rol === "JUGADOR") {
        const selCapitan = document.getElementById("equipo-capitan");
        if (selCapitan) {
            selCapitan.value = usuarioActual.id;
            listaMiembrosFormulario = [{
                usuarioId: usuarioActual.id,
                apodo: usuarioActual.apodo,
                rol: "Capitan"
            }];
            renderizarMiembrosFormulario();
        }
    }
}

function agregarMiembroALista() {
    const selUsuario = document.getElementById("miembro-usuario-select");
    const inputRol = document.getElementById("miembro-rol-input");
    const errorMiembro = document.getElementById("error-agregar-miembro");

    const usuarioId = parseInt(selUsuario.value, 10);
    const rol = (inputRol.value || "Titular").trim();

    if (!usuarioId) {
        mostrarErrorMiembro("Selecciona un jugador para agregar a la escuadra.");
        return;
    }

    const yaExiste = listaMiembrosFormulario.some(m => m.usuarioId === usuarioId);
    if (yaExiste) {
        mostrarErrorMiembro("Este jugador ya forma parte de los integrantes de la escuadra.");
        return;
    }

    const usuario = ArenaStorage.getUsuarios().find(u => u.id === usuarioId);
    if (!usuario) return;

    listaMiembrosFormulario.push({
        usuarioId: usuario.id,
        apodo: usuario.apodo,
        rol: rol
    });

    selUsuario.value = "";
    inputRol.value = "";
    if (errorMiembro) errorMiembro.style.display = "none";

    renderizarMiembrosFormulario();
}

function mostrarErrorMiembro(mensaje) {
    const errorSpan = document.getElementById("error-agregar-miembro");
    if (errorSpan) {
        errorSpan.textContent = mensaje;
        errorSpan.style.display = "block";
    }
}

function renderizarMiembrosFormulario() {
    const contenedor = document.getElementById("lista-miembros-creacion");
    const contador = document.getElementById("contador-miembros-formulario");
    if (!contenedor) return;

    if (contador) contador.textContent = `(${listaMiembrosFormulario.length} miembros)`;
    contenedor.innerHTML = "";

    if (listaMiembrosFormulario.length === 0) {
        contenedor.innerHTML = `<li style="color: var(--color-texto-muted); padding: 0.5rem 0;">No has agregado integrantes aun.</li>`;
        return;
    }

    listaMiembrosFormulario.forEach((m, index) => {
        const li = document.createElement("li");
        li.style.display = "flex";
        li.style.justifyContent = "space-between";
        li.style.alignItems = "center";
        li.style.padding = "0.5rem 0.75rem";
        li.style.marginBottom = "0.4rem";
        li.style.background = "var(--color-fondo)";
        li.style.borderRadius = "var(--radio-borde-sm)";
        li.style.border = "1px solid var(--color-borde)";

        li.innerHTML = `
            <div>
                <strong>${m.apodo}</strong>
                <span class="badge ${m.rol === 'Capitan' ? 'badge-abierto' : 'badge-finalizado'}" style="margin-left: 0.5rem;">
                    ${m.rol}
                </span>
            </div>
            <div>
                ${m.rol !== 'Capitan' ? `<button type="button" class="boton boton-peligro boton-sm" style="padding: 0.2rem 0.5rem; font-size: 0.75rem;" onclick="quitarMiembro(${index})">Quitar</button>` : '<span style="font-size: 0.8rem; color: var(--color-acento);">Capitan Principal</span>'}
            </div>
        `;
        contenedor.appendChild(li);
    });
}

window.quitarMiembro = function(index) {
    listaMiembrosFormulario.splice(index, 1);
    renderizarMiembrosFormulario();
};

function manejarCreacionEquipo(e) {
    e.preventDefault();

    const nombreInput = document.getElementById("equipo-nombre");
    const juegoSelect = document.getElementById("equipo-juego");
    const capitanSelect = document.getElementById("equipo-capitan");
    const alertaGlobal = document.getElementById("alerta-equipo-global");

    const nombre = nombreInput.value.trim();
    const juegoId = parseInt(juegoSelect.value, 10);
    const capitanId = parseInt(capitanSelect.value, 10);

    let esValido = true;
    const errores = [];

    if (!nombre || nombre.length < 3) {
        marcarInvalidoEquipo("grupo-equipo-nombre", "El nombre del equipo es obligatorio y debe tener al menos 3 caracteres.");
        errores.push("El nombre de la escuadra debe tener al menos 3 caracteres.");
        esValido = false;
    } else {
        const equipos = ArenaStorage.getEquipos();
        const yaExisteNombre = equipos.some(eq => eq.nombre.toLowerCase() === nombre.toLowerCase());
        if (yaExisteNombre) {
            marcarInvalidoEquipo("grupo-equipo-nombre", "Ya existe un equipo registrado con ese nombre en la plataforma.");
            errores.push("Nombre de equipo ya en uso.");
            esValido = false;
        } else {
            marcarValidoEquipo("grupo-equipo-nombre");
        }
    }

    if (!juegoId) {
        marcarInvalidoEquipo("grupo-equipo-juego", "Debes seleccionar el juego principal del equipo.");
        errores.push("Selecciona el titulo o disciplina del equipo.");
        esValido = false;
    } else {
        marcarValidoEquipo("grupo-equipo-juego");
    }

    if (!capitanId) {
        marcarInvalidoEquipo("grupo-equipo-capitan", "El equipo debe tener un Capitan asignado obligatoriamente.");
        errores.push("Capitan obligatorio no asignado.");
        esValido = false;
    } else {
        marcarValidoEquipo("grupo-equipo-capitan");
    }

    if (juegoId) {
        const juego = ArenaStorage.getJuegos().find(j => j.id === juegoId);
        if (juego && listaMiembrosFormulario.length < juego.minIntegrantes) {
            errores.push(`Se requieren al menos ${juego.minIntegrantes} integrantes para ${juego.nombre}. Actualmente tienes ${listaMiembrosFormulario.length}.`);
            esValido = false;
        }
    }

    if (!esValido) {
        if (alertaGlobal) {
            alertaGlobal.style.display = "flex";
            alertaGlobal.className = "alerta alerta-error";
            alertaGlobal.innerHTML = `
                <div>
                    <strong>Error al crear la escuadra:</strong>
                    <ul style="margin-top: 0.35rem; padding-left: 1.25rem;">
                        ${errores.map(err => `<li>${err}</li>`).join("")}
                    </ul>
                </div>
            `;
        }
        mostrarToast("Revisa los requisitos del formulario.", "error");
        return;
    }

    const equipos = ArenaStorage.getEquipos();
    const nuevoEquipo = {
        id: Date.now(),
        nombre: nombre,
        juegoId: juegoId,
        capitanId: capitanId,
        activo: true,
        fechaCreacion: new Date().toISOString().split("T")[0],
        integrantes: [...listaMiembrosFormulario]
    };

    equipos.push(nuevoEquipo);
    ArenaStorage.saveEquipos(equipos);

    if (alertaGlobal) {
        alertaGlobal.style.display = "flex";
        alertaGlobal.className = "alerta alerta-exito";
        alertaGlobal.innerHTML = `
            <div>
                <strong>Equipo creado con exito</strong>
                <p>La escuadra <strong>${nombre}</strong> ha sido registrada con ${listaMiembrosFormulario.length} integrantes y esta lista para inscribirse en torneos.</p>
            </div>
        `;
    }

    mostrarToast(`Escuadra ${nombre} registrada correctamente`, "exito");
    document.getElementById("formulario-crear-equipo").reset();
    listaMiembrosFormulario = [];
    renderizarMiembrosFormulario();
    renderizarListaEquipos();
}

function marcarInvalidoEquipo(id, msg) {
    const grupo = document.getElementById(id);
    if (!grupo) return;
    grupo.classList.add("invalido");
    grupo.classList.remove("valido");
    const err = grupo.querySelector(".mensaje-error");
    if (err) {
        err.textContent = msg;
        err.style.display = "flex";
    }
}

function marcarValidoEquipo(id) {
    const grupo = document.getElementById(id);
    if (!grupo) return;
    grupo.classList.remove("invalido");
    grupo.classList.add("valido");
    const err = grupo.querySelector(".mensaje-error");
    if (err) {
        err.textContent = "";
        err.style.display = "none";
    }
}

function renderizarListaEquipos() {
    const contenedor = document.getElementById("grilla-equipos-registrados");
    if (!contenedor) return;

    const equipos = ArenaStorage.getEquipos();
    const juegos = ArenaStorage.getJuegos();
    contenedor.innerHTML = "";

    if (equipos.length === 0) {
        contenedor.innerHTML = `<div class="estado-vacio" style="grid-column: 1 / -1;"><p>No hay equipos registrados aun.</p></div>`;
        return;
    }

    equipos.forEach(eq => {
        const juego = juegos.find(j => j.id === eq.juegoId);
        const tarjeta = document.createElement("article");
        tarjeta.className = "tarjeta";

        tarjeta.innerHTML = `
            <div class="tarjeta-cuerpo">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.75rem;">
                    <span class="badge ${eq.activo ? 'badge-abierto' : 'badge-finalizado'}">${eq.activo ? 'Activo' : 'Inactivo'}</span>
                </div>
                <h3 class="tarjeta-titulo">${eq.nombre}</h3>
                <div class="tarjeta-metadatos">
                    <div><strong>Disciplina:</strong> ${juego ? juego.nombre : 'General'}</div>
                    <div><strong>Plantel:</strong> ${eq.integrantes.length} jugadores</div>
                    <div><strong>Fundado:</strong> ${formatearFecha(eq.fechaCreacion)}</div>
                </div>

                <div style="background: var(--color-fondo); border: 1px solid var(--color-borde); border-radius: var(--radio-borde-sm); padding: 0.75rem; margin-bottom: 1rem;">
                    <div style="font-size: 0.8rem; color: var(--color-acento); font-weight: bold; margin-bottom: 0.35rem;">Roster Oficial:</div>
                    <ul style="list-style: none; font-size: 0.85rem; padding: 0;">
                        ${eq.integrantes.map(m => `<li>&bull; <strong>${m.apodo}</strong> <span style="color: var(--color-texto-muted);">(${m.rol})</span></li>`).join("")}
                    </ul>
                </div>

                <div class="tarjeta-pie">
                    <a href="inscripcion.html" class="boton boton-primario boton-sm boton-completo">Inscribir en Torneo</a>
                </div>
            </div>
        `;
        contenedor.appendChild(tarjeta);
    });
}
