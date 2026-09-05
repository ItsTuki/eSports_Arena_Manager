/**
 * eSports Arena Manager - Logica Global y Utilidades (EP1)
 */

document.addEventListener("DOMContentLoaded", () => {
    inicializarNavegacion();
    inicializarSimuladorSesion();
});

/**
 * Resalta el enlace activo en la barra de navegacion segun la URL actual.
 */
function inicializarNavegacion() {
    const rutaActual = window.location.pathname.split("/").pop() || "index.html";
    const enlaces = document.querySelectorAll(".menu-navegacion a");
    
    enlaces.forEach(enlace => {
        const href = enlace.getAttribute("href");
        if (href === rutaActual || (rutaActual === "" && href === "index.html")) {
            enlace.classList.add("activo");
            enlace.setAttribute("aria-current", "page");
        } else {
            enlace.classList.remove("activo");
            enlace.removeAttribute("aria-current");
        }
    });
}

/**
 * Controla el selector de cambio de usuario / rol simulado en la barra superior.
 */
function inicializarSimuladorSesion() {
    const selector = document.getElementById("selector-usuario-simulado");
    if (!selector) return;

    const usuarios = ArenaStorage.getUsuarios();
    const usuarioActual = ArenaStorage.getUsuarioActual() || usuarios[0];

    // Llenar selector si esta vacio
    if (selector.options.length === 0) {
        usuarios.forEach(user => {
            const opt = document.createElement("option");
            opt.value = user.id;
            opt.textContent = `${user.apodo} (${user.rol})`;
            if (user.id === usuarioActual.id) {
                opt.selected = true;
            }
            selector.appendChild(opt);
        });
    }

    selector.addEventListener("change", (e) => {
        const usuarioSeleccionado = usuarios.find(u => u.id === parseInt(e.target.value, 10));
        if (usuarioSeleccionado) {
            ArenaStorage.setUsuarioActual(usuarioSeleccionado);
            mostrarToast(`Sesion cambiada a: ${usuarioSeleccionado.apodo} [${usuarioSeleccionado.rol}]`, "info");
            window.dispatchEvent(new CustomEvent("cambio-usuario-simulado", { detail: usuarioSeleccionado }));
            setTimeout(() => {
                if (window.location.pathname.includes("perfil.html") || window.location.pathname.includes("inscripcion.html")) {
                    window.location.reload();
                }
            }, 500);
        }
    });
}

/**
 * Genera el badge HTML para el estado de un torneo.
 */
function generarBadgeEstado(estado) {
    switch (estado) {
        case "ABIERTO":
            return `<span class="badge badge-abierto">Inscripciones Abiertas</span>`;
        case "EN_CURSO":
            return `<span class="badge badge-en-curso">En Curso</span>`;
        case "FINALIZADO":
            return `<span class="badge badge-finalizado">Finalizado</span>`;
        case "CANCELADO":
            return `<span class="badge badge-finalizado">Cancelado</span>`;
        default:
            return `<span class="badge badge-finalizado">${estado}</span>`;
    }
}

/**
 * Formatea una fecha ISO (YYYY-MM-DD) a formato legible en espanol.
 */
function formatearFecha(fechaStr) {
    if (!fechaStr) return "N/A";
    const partes = fechaStr.split("-");
    if (partes.length !== 3) return fechaStr;
    const meses = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
    const anio = partes[0];
    const mes = meses[parseInt(partes[1], 10) - 1];
    const dia = partes[2];
    return `${dia} ${mes}, ${anio}`;
}

/**
 * Notificacion toast simple.
 */
function mostrarToast(mensaje, tipo = "info") {
    let toast = document.getElementById("arena-toast");
    if (!toast) {
        toast = document.createElement("div");
        toast.id = "arena-toast";
        toast.style.position = "fixed";
        toast.style.bottom = "20px";
        toast.style.right = "20px";
        toast.style.zIndex = "9999";
        toast.style.padding = "10px 18px";
        toast.style.borderRadius = "6px";
        toast.style.fontWeight = "600";
        toast.style.fontSize = "0.9rem";
        toast.style.boxShadow = "0 4px 12px rgba(0,0,0,0.5)";
        toast.style.transition = "opacity 0.3s ease, transform 0.3s ease";
        document.body.appendChild(toast);
    }

    if (tipo === "error") {
        toast.style.backgroundColor = "#FF5C7A";
        toast.style.color = "#FFFFFF";
    } else if (tipo === "exito") {
        toast.style.backgroundColor = "#00F5D4";
        toast.style.color = "#0E0B16";
    } else {
        toast.style.backgroundColor = "#9146FF";
        toast.style.color = "#FFFFFF";
    }

    toast.textContent = mensaje;
    toast.style.opacity = "1";
    toast.style.transform = "translateY(0)";

    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(10px)";
    }, 3500);
}
