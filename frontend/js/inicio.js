/**
 * eSports Arena Manager - Logica Vista de Inicio (index.html)
 */

document.addEventListener("DOMContentLoaded", () => {
    renderizarTorneosDestacados();
    renderizarProximosCierres();
});

/**
 * Renderiza dinamicamente los torneos destacados desde los datos simulados.
 */
function renderizarTorneosDestacados() {
    const contenedor = document.getElementById("contenedor-torneos-destacados");
    if (!contenedor) return;

    const torneos = ArenaStorage.getTorneos();
    const destacados = torneos.filter(t => t.estado === "ABIERTO" || t.estado === "EN_CURSO").slice(0, 3);

    contenedor.innerHTML = "";

    if (destacados.length === 0) {
        contenedor.innerHTML = `
            <div class="estado-vacio" style="grid-column: 1 / -1;">
                <h3>No hay torneos activos en este momento</h3>
                <p>Proximamente se publicaran nuevas fechas competitivas.</p>
            </div>
        `;
        return;
    }

    destacados.forEach(torneo => {
        const tarjeta = document.createElement("article");
        tarjeta.className = "tarjeta";

        const cuposDisponibles = ArenaStorage.calcularCuposDisponibles(torneo);
        const estaLleno = cuposDisponibles === 0;

        tarjeta.innerHTML = `
            <div class="tarjeta-media">
                <img src="${torneo.banner}" alt="Banner del torneo ${torneo.nombre}" loading="lazy">
                <div class="badge-flotante">
                    ${generarBadgeEstado(torneo.estado)}
                </div>
            </div>
            <div class="tarjeta-cuerpo">
                <h3 class="tarjeta-titulo">${torneo.nombre}</h3>
                <div class="tarjeta-metadatos">
                    <div><strong>Juego:</strong> ${torneo.juegoNombre} (${torneo.modalidad})</div>
                    <div><strong>Fecha Inicio:</strong> ${formatearFecha(torneo.fechaInicio)}</div>
                    <div><strong>Cupos:</strong> ${torneo.cuposOcupados}/${torneo.cupoMaximo} ${estaLleno ? '<span style="color:var(--color-error); font-weight:bold;">(Lleno)</span>' : `(${cuposDisponibles} libres)`}</div>
                    <div><strong>Cierre Inscripcion:</strong> ${formatearFecha(torneo.cierreInscripcion)}</div>
                </div>
                <div class="tarjeta-pie">
                    <a href="detalle-torneo.html?id=${torneo.id}" class="boton boton-secundario boton-sm">Ver Detalle</a>
                    ${torneo.estado === "ABIERTO" && !estaLleno
                        ? `<a href="inscripcion.html?torneoId=${torneo.id}" class="boton boton-primario boton-sm">Inscribirme</a>`
                        : `<button class="boton boton-secundario boton-sm" disabled>No disponible</button>`
                    }
                </div>
            </div>
        `;
        contenedor.appendChild(tarjeta);
    });
}

/**
 * Renderiza el bloque de proximos cierres de inscripcion ordenados por fecha.
 */
function renderizarProximosCierres() {
    const contenedor = document.getElementById("lista-proximos-cierres");
    if (!contenedor) return;

    const torneos = ArenaStorage.getTorneos();
    const torneosAbiertos = torneos
        .filter(t => t.estado === "ABIERTO")
        .sort((a, b) => new Date(a.cierreInscripcion) - new Date(b.cierreInscripcion));

    contenedor.innerHTML = "";

    if (torneosAbiertos.length === 0) {
        contenedor.innerHTML = `<li style="color: var(--color-texto-muted); padding: 0.5rem 0;">No hay convocatorias proximas abiertas.</li>`;
        return;
    }

    torneosAbiertos.forEach(t => {
        const item = document.createElement("li");
        item.style.padding = "0.75rem 0";
        item.style.borderBottom = "1px solid rgba(255, 255, 255, 0.08)";
        item.style.display = "flex";
        item.style.justifyContent = "space-between";
        item.style.alignItems = "center";
        item.style.flexWrap = "wrap";
        item.style.gap = "0.5rem";

        const cuposLibres = ArenaStorage.calcularCuposDisponibles(t);

        item.innerHTML = `
            <div>
                <strong style="color: #FFFFFF;">${t.nombre}</strong>
                <div style="font-size: 0.85rem; color: var(--color-texto-secundario);">
                    Juego: ${t.juegoNombre} | Cierra: <span style="color: var(--color-acento); font-weight: bold;">${formatearFecha(t.cierreInscripcion)}</span>
                </div>
            </div>
            <div>
                <span class="badge ${cuposLibres > 0 ? 'badge-abierto' : 'badge-finalizado'}" style="margin-right: 0.5rem;">
                    ${cuposLibres > 0 ? `${cuposLibres} cupos` : 'Agotado'}
                </span>
                <a href="inscripcion.html?torneoId=${t.id}" class="boton boton-primario boton-sm" style="padding: 0.25rem 0.6rem; font-size: 0.8rem;">Inscribir</a>
            </div>
        `;
        contenedor.appendChild(item);
    });
}
