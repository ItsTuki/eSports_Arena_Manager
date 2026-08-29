# Documento de Cobertura de Testing Unitario (EP2)
## eSports Arena Manager

**Framework de Pruebas:** Jasmine 5.1.1  
**Ejecutor / Runner:** Karma 6.4.2 & Jasmine Browser SpecRunner  
**Estado:** 10 / 10 Pruebas Exitosas (100% de Aprobación)

---

## 1. Resumen Ejecutivo
Para la Evaluación Parcial 2 (EP2) se implementaron las **diez (10) pruebas unitarias obligatorias** definidas en las especificaciones del caso de estudio. Las pruebas evalúan tanto funciones puras de lógica y cálculo de puntuación como el montaje, renderizado condicional y manejo de eventos/espías de componentes React y manipulación del DOM.

---

## 2. Detalle de los 10 Casos de Prueba

| N° | Nombre del Caso | Archivo Fuente / Componente | Descripción de la Prueba | Resultado |
|---|---|---|---|---|
| **1** | `ordenarRanking` | `js/tournamentUtils.js` | Verifica ordenación por puntos descendente y desempate por diferencia de puntaje. | **PASSED** |
| **2** | `calcularPuntos` | `js/tournamentUtils.js` | Comprueba asignación correcta de puntos por victoria (+3), empate (+1) y derrota (+0). | **PASSED** |
| **3** | `cuposDisponibles` | `js/tournamentUtils.js` | Comprueba que resta inscripciones al cupo máximo y nunca retorna valores negativos. | **PASSED** |
| **4** | `inscripcionFueraDePlazo` | `js/tournamentUtils.js` | Valida retorno de `true` si la fecha actual supera el cierre de inscripciones. | **PASSED** |
| **5** | `tieneSancionActiva` | `js/tournamentUtils.js` | Bloquea al participante con sanción vigente y habilita al que ya la cumplió. | **PASSED** |
| **6** | `equipoCompleto` | `js/tournamentUtils.js` | Verifica que el equipo cumpla con el número mínimo de integrantes según el juego (ej: 5 para Valorant). | **PASSED** |
| **7** | `TarjetaTorneo` | `tests/specs/components.spec.js` | Comprueba el renderizado en el DOM del nombre, juego habilitado y badge de estado. | **PASSED** |
| **8** | `TablaRanking` | `tests/specs/components.spec.js` | Verifica que se renderice una fila por participante y se respete el orden recibido. | **PASSED** |
| **9** | `FormularioInscripcion` | `tests/specs/components.spec.js` | Comprueba botón deshabilitado si el equipo está incompleto, usando un espía (`jasmine.createSpy`). | **PASSED** |
| **10** | `LlaveTorneo` | `tests/specs/components.spec.js` | Verifica despliegue del texto placeholder "Por definir" (TBD) cuando no hay rival asignado. | **PASSED** |

---

## 3. Instrucciones de Ejecución de Pruebas

### Modo 1: Ejecución Interactiva en el Navegador
1. Abrir directamente el archivo:
   ```text
   C:\Users\tuki\OneDrive\Escritorio\eSports_Arena_Manager\frontend\tests\SpecRunner.html
   ```
2. El navegador ejecutará inmediatamente las 10 pruebas unitarias con reporte gráfico en tiempo real con la identidad visual del proyecto.

### Modo 2: Ejecución mediante Karma CLI
```bash
cd C:\Users\tuki\OneDrive\Escritorio\eSports_Arena_Manager\frontend
npm test
```
