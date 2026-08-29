// Validaciones JavaScript para formularios en EP1
document.addEventListener("DOMContentLoaded", function() {
  const formInscripcion = document.getElementById("form-inscripcion-ep1");
  if (formInscripcion) {
    formInscripcion.addEventListener("submit", function(e) {
      e.preventDefault();
      let valido = true;

      const nombreEquipo = document.getElementById("nombre-equipo");
      const errorEquipo = document.getElementById("error-equipo");
      const emailCapitan = document.getElementById("email-capitan");
      const errorEmail = document.getElementById("error-email");
      const numJugadores = document.getElementById("num-jugadores");
      const errorJugadores = document.getElementById("error-jugadores");

      // Validar nombre equipo
      if (!nombreEquipo.value.trim()) {
        errorEquipo.textContent = "El nombre del equipo es obligatorio.";
        errorEquipo.classList.add("visible");
        valido = false;
      } else {
        errorEquipo.classList.remove("visible");
      }

      // Validar email
      const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!regexEmail.test(emailCapitan.value.trim())) {
        errorEmail.textContent = "Ingrese un correo electrónico válido.";
        errorEmail.classList.add("visible");
        valido = false;
      } else {
        errorEmail.classList.remove("visible");
      }

      // Validar num jugadores
      const num = Number(numJugadores.value);
      if (isNaN(num) || num < 5) {
        errorJugadores.textContent = "El juego exige un mínimo de 5 integrantes.";
        errorJugadores.classList.add("visible");
        valido = false;
      } else {
        errorJugadores.classList.remove("visible");
      }

      if (valido) {
        alert("Inscripción validada y registrada correctamente (EP1).");
        formInscripcion.reset();
      }
    });
  }
});
