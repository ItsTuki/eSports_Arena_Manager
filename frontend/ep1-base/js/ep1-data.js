// Datos simulados para carga dinámica mediante manipulación del DOM en EP1
const CATALOGO_TORNEOS = [
  {
    id: 1,
    nombre: "Supercopa Valorant 2026",
    juego: "Valorant Masters",
    estado: "ABIERTO",
    cupoMaximo: 16,
    cuposOcupados: 8,
    cierreInscripcion: "2026-09-08",
    premio: "$2,500,000 CLP"
  },
  {
    id: 2,
    nombre: "Circuito de Leyendas LoL",
    juego: "League of Legends",
    estado: "EN_CURSO",
    cupoMaximo: 8,
    cuposOcupados: 8,
    cierreInscripcion: "2026-08-15",
    premio: "$3,000,000 CLP"
  },
  {
    id: 3,
    nombre: "Torneo Relampago Rocket League",
    juego: "Rocket League",
    estado: "FINALIZADO",
    cupoMaximo: 8,
    cuposOcupados: 8,
    cierreInscripcion: "2026-07-28",
    premio: "$1,000,000 CLP"
  }
];

function renderizarTorneos(contenedorId) {
  const contenedor = document.getElementById(contenedorId);
  if (!contenedor) return;

  contenedor.innerHTML = "";
  CATALOGO_TORNEOS.forEach(torneo => {
    const tarjeta = document.createElement("article");
    tarjeta.className = "tarjeta";
    tarjeta.innerHTML = `
      <div style="display:flex; justify-content:space-between; margin-bottom:0.5rem;">
        <span class="insignia insignia-abierto">${torneo.juego}</span>
        <span class="insignia" style="background:#332356;">${torneo.estado}</span>
      </div>
      <h3 style="margin-bottom:0.5rem;">${torneo.nombre}</h3>
      <p style="color:#a89bc4; font-size:14px; margin-bottom:1rem;">Bolsa de Premios: <strong style="color:var(--color-acento)">${torneo.premio}</strong></p>
      <div style="background:#120c22; padding:0.75rem; border-radius:6px; margin-bottom:1rem; font-size:13px;">
        <div>Cupos: <strong>${torneo.cuposOcupados}/${torneo.cupoMaximo}</strong></div>
        <div>Cierre: <strong>${torneo.cierreInscripcion}</strong></div>
      </div>
      <a href="detalle-torneo.html?id=${torneo.id}" class="boton-secundario" style="width:100%; text-align:center;">Ver Detalle</a>
    `;
    contenedor.appendChild(tarjeta);
  });
}
