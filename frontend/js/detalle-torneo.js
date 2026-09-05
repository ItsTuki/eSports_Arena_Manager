/**
 * eSports Arena Manager - Logica Detalle de Torneo (detalle-torneo.html)
 */

let torneoActual = null;
let rondaSeleccionada = "TODAS";

document.addEventListener("DOMContentLoaded", () => {
    cargarSelectorTorneos();
    cargarTorneoDesdeUrl();
});

/**
 * Llena el selector rapido de torneos en la barra superior del detalle.
 */
function cargarSelectorTorneos() {
    const selector = document.getElementById("selector-torneo-detalle");
    if (!selector) return;

    const torneos = ArenaStorage.getTorneos();
    selector.innerHTML = "";
    torneos.forEach(t => {
        const opt = document.createElement("option");
        opt.value = t.id;
        opt.textContent = `${t.nombre} [${t.juegoNombre}]`;
        selector.appendChild(opt);
    });

    selector.addEventListener("change", (e) => {
        cargarDetalleTorneo(parseInt(e.target.value, 10));
    });
}

/**
 * Obtiene el ID del torneo desde la query string (?id=1) o carga el primero.
 */
function cargarTorneoDesdeUrl() {
    const params = new URLSearchParams(window.location.search);
    let torneoId = parseInt(params.get("id"), 10);
    const torneos = ArenaStorage.getTorneos();

    if (!torneoId || isNaN(torneoId) || !torneos.find(t => t.id === torneoId)) {
        torneoId = torneos[0]?.id || 1;
    }

    const selector = document.getElementById("selector-torneo-detalle");
    if (selector) selector.value = torneoId;

    cargarDetalleTorneo(torneoId);
}

/**
 * Renderiza todos los bloques de informacion del torneo seleccionado.
 */
function cargarDetalleTorneo(id) {
    torneoActual = ArenaStorage.getTorneoById(id);
    if (!torneoActual) return;

    // 1. Datos Generales y Banner
    document.getElementById("detalle-banner").src = torneoActual.banner;
    document.getElementById("detalle-banner").alt = `Banner del torneo ${torneoActual.nombre}`;
    document.getElementById("detalle-titulo").textContent = torneoActual.nombre;
    document.getElementById("detalle-estado-badge").innerHTML = generarBadgeEstado(torneoActual.estado);
    document.getElementById("detalle-juego").textContent = `${torneoActual.juegoNombre} (${torneoActual.modalidad})`;
    document.getElementById("detalle-fechas").textContent = `${formatearFecha(torneoActual.fechaInicio)} al ${formatearFecha(torneoActual.fechaFin)}`;
    document.getElementById("detalle-cierre").textContent = formatearFecha(torneoActual.cierreInscripcion);
    document.getElementById("detalle-organizador").textContent = torneoActual.organizador;
    document.getElementById("detalle-descripcion").textContent = torneoActual.descripcion;

    // Cupos
    const cuposLibres = ArenaStorage.calcularCuposDisponibles(torneoActual);
    const estaLleno = cuposLibres === 0;
    const porcentajeOcupacion = Math.min(100, Math.round((torneoActual.cuposOcupados / torneoActual.cupoMaximo) * 100));

    document.getElementById("detalle-cupos-texto").innerHTML = `
        <strong>${torneoActual.cuposOcupados} / ${torneoActual.cupoMaximo}</strong> inscritos 
        (${cuposLibres} libres)
    `;
    const barraProgreso = document.getElementById("barra-cupos-progreso");
    if (barraProgreso) {
        barraProgreso.style.width = `${porcentajeOcupacion}%`;
        barraProgreso.style.backgroundColor = estaLleno ? "var(--color-error)" : "var(--color-acento)";
    }

    // Boton de accion
    const btnInscripcion = document.getElementById("btn-ir-inscripcion");
    if (btnInscripcion) {
        if (torneoActual.estado === "ABIERTO" && !estaLleno) {
            btnInscripcion.href = `inscripcion.html?torneoId=${torneoActual.id}`;
            btnInscripcion.style.display = "inline-flex";
            btnInscripcion.textContent = "Inscribirse en este Torneo";
            btnInscripcion.classList.remove("boton-secundario");
            btnInscripcion.classList.add("boton-primario");
        } else {
            btnInscripcion.style.display = "none";
        }
    }

    // 2. Requisitos
    renderizarRequisitos(torneoActual.requisitos);

    // 3. Participantes Inscritos
    renderizarInscritos(torneoActual.inscritos);

    // 4. Llaves y Partidas por Ronda
    renderizarPartidasYRondas(torneoActual.partidas);

    // 5. Tabla de Posiciones / Ranking
    renderizarRanking(torneoActual.ranking);

    // 6. Premios
    renderizarPremios(torneoActual.premios, torneoActual.estado);
}

function renderizarRequisitos(requisitos = []) {
    const lista = document.getElementById("detalle-requisitos");
    if (!lista) return;
    lista.innerHTML = "";
    requisitos.forEach(req => {
        const li = document.createElement("li");
        li.style.marginBottom = "0.5rem";
        li.innerHTML = `<span>&bull; ${req}</span>`;
        lista.appendChild(li);
    });
}

function renderizarInscritos(inscritos = []) {
    const contenedor = document.getElementById("detalle-lista-inscritos");
    const contador = document.getElementById("detalle-contador-inscritos");
    if (!contenedor) return;

    if (contador) contador.textContent = `(${inscritos.length})`;
    contenedor.innerHTML = "";

    if (inscritos.length === 0) {
        contenedor.innerHTML = `<li style="color: var(--color-texto-muted);">No hay participantes registrados todavia.</li>`;
        return;
    }

    inscritos.forEach((part, index) => {
        const li = document.createElement("li");
        li.style.padding = "0.5rem 0";
        li.style.borderBottom = "1px solid rgba(255, 255, 255, 0.05)";
        li.style.display = "flex";
        li.style.justifyContent = "space-between";
        li.style.alignItems = "center";
        li.innerHTML = `
            <div>
                <span style="color: var(--color-acento); font-weight: bold; margin-right: 0.5rem;">#${index + 1}</span>
                <strong>${part.nombre}</strong>
                <span style="font-size: 0.8rem; color: var(--color-texto-muted); margin-left: 0.5rem;">(${part.tipo})</span>
            </div>
            <span style="font-size: 0.8rem; color: var(--color-texto-secundario);">${formatearFecha(part.fecha)}</span>
        `;
        contenedor.appendChild(li);
    });
}

function renderizarPartidasYRondas(partidas = []) {
    const contenedorPestanas = document.getElementById("pestanas-rondas-partidas");
    const contenedorPartidas = document.getElementById("contenedor-lista-partidas");
    if (!contenedorPestanas || !contenedorPartidas) return;

    const rondasUnicas = ["TODAS", ...new Set(partidas.map(p => p.ronda))];

    contenedorPestanas.innerHTML = "";
    rondasUnicas.forEach(ronda => {
        const btn = document.createElement("button");
        btn.type = "button";
        btn.className = `pestana-btn ${ronda === rondaSeleccionada ? 'activa' : ''}`;
        btn.textContent = ronda;
        btn.addEventListener("click", () => {
            rondaSeleccionada = ronda;
            document.querySelectorAll("#pestanas-rondas-partidas .pestana-btn").forEach(b => b.classList.remove("activa"));
            btn.classList.add("activa");
            mostrarPartidasFiltradasPorRonda(partidas);
        });
        contenedorPestanas.appendChild(btn);
    });

    mostrarPartidasFiltradasPorRonda(partidas);
}

function mostrarPartidasFiltradasPorRonda(partidas) {
    const contenedor = document.getElementById("contenedor-lista-partidas");
    if (!contenedor) return;

    const partidasAMostrar = rondaSeleccionada === "TODAS"
        ? partidas
        : partidas.filter(p => p.ronda === rondaSeleccionada);

    contenedor.innerHTML = "";

    if (partidasAMostrar.length === 0) {
        contenedor.innerHTML = `<p style="color: var(--color-texto-muted); padding: 1rem 0;">No hay partidas programadas para esta ronda.</p>`;
        return;
    }

    partidasAMostrar.forEach(partida => {
        const tarjeta = document.createElement("div");
        tarjeta.className = "partida-tarjeta";

        let badgePartida = `<span class="badge badge-abierto">Programada</span>`;
        if (partida.estado === "FINALIZADA") {
            badgePartida = `<span class="badge badge-finalizado">Finalizada</span>`;
        } else if (partida.estado === "EN_JUEGO") {
            badgePartida = `<span class="badge badge-en-curso">En Juego</span>`;
        }

        tarjeta.innerHTML = `
            <div>
                <div style="font-size: 0.8rem; color: var(--color-acento); font-weight: bold; margin-bottom: 0.35rem;">
                    ${partida.ronda} &bull; Horario: ${partida.fechaHora}
                </div>
                <div class="partida-equipos">
                    <span>${partida.equipoA}</span>
                    <span class="partida-vs">VS</span>
                    <span>${partida.equipoB}</span>
                </div>
            </div>
            <div style="text-align: right;">
                <div style="margin-bottom: 0.4rem;">${badgePartida}</div>
                <div style="font-size: 0.9rem; font-weight: bold; color: var(--color-texto-secundario);">
                    ${partida.resultado ? partida.resultado : "Pendiente de juego"}
                </div>
            </div>
        `;
        contenedor.appendChild(tarjeta);
    });
}

function renderizarRanking(ranking = []) {
    const cuerpoTabla = document.getElementById("cuerpo-tabla-ranking");
    if (!cuerpoTabla) return;

    const rankingOrdenado = [...ranking].sort((a, b) => {
        if (b.puntos !== a.puntos) {
            return b.puntos - a.puntos;
        }
        return b.difPuntos - a.difPuntos;
    });

    cuerpoTabla.innerHTML = "";

    if (rankingOrdenado.length === 0) {
        cuerpoTabla.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--color-texto-muted); padding: 1.5rem;">No hay resultados registrados en la tabla.</td></tr>`;
        return;
    }

    rankingOrdenado.forEach((item, index) => {
        const tr = document.createElement("tr");
        let clasePodio = "";
        let posTexto = `${index + 1}°`;

        if (index === 0) {
            clasePodio = "podio-oro";
            posTexto = "1° (Oro)";
        } else if (index === 1) {
            clasePodio = "podio-plata";
            posTexto = "2° (Plata)";
        } else if (index === 2) {
            clasePodio = "podio-bronce";
            posTexto = "3° (Bronce)";
        }

        tr.innerHTML = `
            <td class="${clasePodio}">${posTexto}</td>
            <td><strong>${item.participante}</strong></td>
            <td>${item.partidasJugadas}</td>
            <td><span style="color: var(--color-acento);">${item.victorias}</span></td>
            <td><span style="color: var(--color-error);">${item.derrotas}</span></td>
            <td>${item.difPuntos > 0 ? `+${item.difPuntos}` : item.difPuntos}</td>
            <td><strong style="color: var(--color-primario); font-size: 1.05rem;">${item.puntos} pts</strong></td>
        `;
        cuerpoTabla.appendChild(tr);
    });
}

function renderizarPremios(premios = [], estadoTorneo) {
    const contenedor = document.getElementById("contenedor-premios");
    if (!contenedor) return;

    contenedor.innerHTML = "";

    const aviso = document.createElement("div");
    aviso.style.fontSize = "0.85rem";
    aviso.style.marginBottom = "1rem";
    aviso.style.color = estadoTorneo === "FINALIZADO" ? "var(--color-acento)" : "var(--color-texto-secundario)";
    aviso.innerHTML = estadoTorneo === "FINALIZADO"
        ? "<strong>Torneo Finalizado:</strong> Premios distribuidos a los ganadores del podio."
        : "<strong>Bolsa de Premios:</strong> Los premios se entregaran al concluir la fase final.";
    contenedor.appendChild(aviso);

    const lista = document.createElement("div");
    lista.style.display = "grid";
    lista.style.gridTemplateColumns = "repeat(auto-fit, minmax(220px, 1fr))";
    lista.style.gap = "1rem";

    premios.forEach(premio => {
        const tarjeta = document.createElement("div");
        tarjeta.style.background = "var(--color-superficie)";
        tarjeta.style.border = "1px solid var(--color-borde)";
        tarjeta.style.borderRadius = "var(--radio-borde)";
        tarjeta.style.padding = "1rem";
        tarjeta.style.borderLeft = premio.posicion === 1 ? "4px solid #FFD700" : (premio.posicion === 2 ? "4px solid #C0C0C0" : "4px solid #CD7F32");

        let tituloPodio = `${premio.posicion}° Lugar`;
        if (premio.posicion === 1) tituloPodio = "1er Lugar (Campeon)";
        if (premio.posicion === 2) tituloPodio = "2do Lugar (Subcampeon)";
        if (premio.posicion === 3) tituloPodio = "3er Lugar";

        tarjeta.innerHTML = `
            <h4 style="color: #FFFFFF; font-size: 1rem; margin-bottom: 0.35rem;">${tituloPodio}</h4>
            <p style="color: var(--color-acento); font-weight: bold; font-size: 0.95rem;">${premio.recompensa}</p>
        `;
        lista.appendChild(tarjeta);
    });

    contenedor.appendChild(lista);
}
