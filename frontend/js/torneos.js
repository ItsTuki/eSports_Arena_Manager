/**
 * eSports Arena Manager - Logica Vista Catalogo de Torneos (torneos.html)
 */

document.addEventListener("DOMContentLoaded", () => {
    cargarOpcionesJuegos();
    leerParametrosUrl();
    configurarEventosFiltros();
    filtrarYRenderizarTorneos();
});

/**
 * Llena el selector de juegos dinamicamente desde los datos de juegos.
 */
function cargarOpcionesJuegos() {
    const selectorJuego = document.getElementById("filtro-juego");
    if (!selectorJuego) return;

    const juegos = ArenaStorage.getJuegos();
    juegos.forEach(j => {
        const opt = document.createElement("option");
        opt.value = j.id;
        opt.textContent = `${j.nombre} (${j.modalidad})`;
        selectorJuego.appendChild(opt);
    });
}

/**
 * Lee posibles parametros GET como `?juego=1` o `?estado=ABIERTO`
 */
function leerParametrosUrl() {
    const params = new URLSearchParams(window.location.search);
    const juegoParam = params.get("juego");
    const estadoParam = params.get("estado");

    if (juegoParam) {
        const selJuego = document.getElementById("filtro-juego");
        if (selJuego) selJuego.value = juegoParam;
    }
    if (estadoParam) {
        const selEstado = document.getElementById("filtro-estado");
        if (selEstado) selEstado.value = estadoParam;
    }
}

/**
 * Configura listeners para busqueda en tiempo real y cambios de filtros.
 */
function configurarEventosFiltros() {
    const buscador = document.getElementById("buscador-nombre");
    const filtroJuego = document.getElementById("filtro-juego");
    const filtroEstado = document.getElementById("filtro-estado");
    const fechaDesde = document.getElementById("filtro-fecha-desde");
    const fechaHasta = document.getElementById("filtro-fecha-hasta");
    const btnLimpiar = document.getElementById("btn-limpiar-filtros");

    if (buscador) buscador.addEventListener("input", filtrarYRenderizarTorneos);
    if (filtroJuego) filtroJuego.addEventListener("change", filtrarYRenderizarTorneos);
    if (filtroEstado) filtroEstado.addEventListener("change", filtrarYRenderizarTorneos);
    if (fechaDesde) fechaDesde.addEventListener("change", filtrarYRenderizarTorneos);
    if (fechaHasta) fechaHasta.addEventListener("change", filtrarYRenderizarTorneos);

    if (btnLimpiar) {
        btnLimpiar.addEventListener("click", () => {
            if (buscador) buscador.value = "";
            if (filtroJuego) filtroJuego.value = "TODOS";
            if (filtroEstado) filtroEstado.value = "TODOS";
            if (fechaDesde) fechaDesde.value = "";
            if (fechaHasta) fechaHasta.value = "";
            ocultarErrorFecha();
            filtrarYRenderizarTorneos();
        });
    }
}

/**
 * Filtra los torneos segun los criterios y maneja validaciones de fecha y estado vacio.
 */
function filtrarYRenderizarTorneos() {
    const contenedor = document.getElementById("grilla-torneos");
    const contador = document.getElementById("contador-resultados");
    if (!contenedor) return;

    const textoBusqueda = (document.getElementById("buscador-nombre")?.value || "").trim().toLowerCase();
    const juegoSeleccionado = document.getElementById("filtro-juego")?.value || "TODOS";
    const estadoSeleccionado = document.getElementById("filtro-estado")?.value || "TODOS";
    const fechaDesde = document.getElementById("filtro-fecha-desde")?.value;
    const fechaHasta = document.getElementById("filtro-fecha-hasta")?.value;

    // Validacion de rango de fechas
    if (fechaDesde && fechaHasta) {
        if (new Date(fechaDesde) > new Date(fechaHasta)) {
            mostrarErrorFecha("La fecha inicial no puede ser posterior a la fecha final de busqueda.");
            contenedor.innerHTML = "";
            if (contador) contador.textContent = "0 torneos encontrados (error en rango de fechas)";
            return;
        } else {
            ocultarErrorFecha();
        }
    } else {
        ocultarErrorFecha();
    }

    const todosLosTorneos = ArenaStorage.getTorneos();

    const filtrados = todosLosTorneos.filter(torneo => {
        if (textoBusqueda && !torneo.nombre.toLowerCase().includes(textoBusqueda) && !torneo.juegoNombre.toLowerCase().includes(textoBusqueda)) {
            return false;
        }
        if (juegoSeleccionado !== "TODOS" && torneo.juegoId !== parseInt(juegoSeleccionado, 10)) {
            return false;
        }
        if (estadoSeleccionado !== "TODOS" && torneo.estado !== estadoSeleccionado) {
            return false;
        }
        if (fechaDesde && new Date(torneo.fechaInicio) < new Date(fechaDesde)) {
            return false;
        }
        if (fechaHasta && new Date(torneo.fechaFin) > new Date(fechaHasta)) {
            return false;
        }
        return true;
    });

    if (contador) {
        contador.textContent = `${filtrados.length} ${filtrados.length === 1 ? 'torneo encontrado' : 'torneos encontrados'}`;
    }

    contenedor.innerHTML = "";

    // Estado vacio
    if (filtrados.length === 0) {
        contenedor.innerHTML = `
            <div class="estado-vacio" style="grid-column: 1 / -1;">
                <h3>No se encontraron torneos</h3>
                <p>No hay competiciones que coincidan con los filtros aplicados. Intenta ajustando los criterios de busqueda.</p>
                <button type="button" class="boton boton-primario boton-sm" style="margin-top: 1rem;" onclick="document.getElementById('btn-limpiar-filtros').click()">Limpiar Filtros</button>
            </div>
        `;
        return;
    }

    filtrados.forEach(torneo => {
        const cuposLibres = ArenaStorage.calcularCuposDisponibles(torneo);
        const estaLleno = cuposLibres === 0;

        const tarjeta = document.createElement("article");
        tarjeta.className = "tarjeta";
        tarjeta.innerHTML = `
            <div class="tarjeta-media">
                <img src="${torneo.banner}" alt="Portada del torneo ${torneo.nombre}" loading="lazy">
                <div class="badge-flotante">
                    ${generarBadgeEstado(torneo.estado)}
                </div>
            </div>
            <div class="tarjeta-cuerpo">
                <h3 class="tarjeta-titulo">${torneo.nombre}</h3>
                <div class="tarjeta-metadatos">
                    <div><strong>Juego:</strong> ${torneo.juegoNombre} (${torneo.modalidad})</div>
                    <div><strong>Fechas:</strong> ${formatearFecha(torneo.fechaInicio)} al ${formatearFecha(torneo.fechaFin)}</div>
                    <div><strong>Cupos:</strong> ${torneo.cuposOcupados}/${torneo.cupoMaximo} ${estaLleno ? '<span style="color:var(--color-error); font-weight:bold;">(Lleno)</span>' : `(${cuposLibres} disponibles)`}</div>
                    <div><strong>Cierre Inscripcion:</strong> ${formatearFecha(torneo.cierreInscripcion)}</div>
                </div>
                <div class="tarjeta-pie">
                    <a href="detalle-torneo.html?id=${torneo.id}" class="boton boton-secundario boton-sm">Ver Detalle</a>
                    ${torneo.estado === "ABIERTO" && !estaLleno
                        ? `<a href="inscripcion.html?torneoId=${torneo.id}" class="boton boton-primario boton-sm">Inscribirme</a>`
                        : `<button class="boton boton-secundario boton-sm" disabled title="Inscripcion no disponible">${estaLleno ? 'Cupo Lleno' : 'Cerrado'}</button>`
                    }
                </div>
            </div>
        `;
        contenedor.appendChild(tarjeta);
    });
}

function mostrarErrorFecha(mensaje) {
    let errorBox = document.getElementById("alerta-error-filtros");
    if (!errorBox) {
        errorBox = document.createElement("div");
        errorBox.id = "alerta-error-filtros";
        errorBox.className = "alerta alerta-error";
        const formFiltros = document.querySelector(".barra-filtros");
        if (formFiltros && formFiltros.parentNode) {
            formFiltros.parentNode.insertBefore(errorBox, formFiltros.nextSibling);
        }
    }
    errorBox.style.display = "flex";
    errorBox.innerHTML = `<span>${mensaje}</span>`;
}

function ocultarErrorFecha() {
    const errorBox = document.getElementById("alerta-error-filtros");
    if (errorBox) {
        errorBox.style.display = "none";
    }
}
