Crea una presentación profesional en español de 16 diapositivas sobre el proyecto académico "Sistema de Gestión de Horarios y Matrícula (SGHM)", para sustentarla ante el docente del curso.

REGLAS VISUALES (OBLIGATORIAS)
- NO uses imágenes generadas con inteligencia artificial en ninguna diapositiva, ni fondos ilustrados con IA.
- Construye lo visual solo con texto, íconos simples, tablas, diagramas de bloques, líneas de tiempo y gráficos nativos de Gamma.
- Donde vaya una captura real del sistema, deja un recuadro vacío con el texto "[Captura de pantalla: …]" para que el equipo la inserte.
- Estilo sobrio y académico: fondo claro, verde institucional oscuro como color principal, dorado como acento, tipografía sans serif limpia. Máximo 6 viñetas por diapositiva y frases cortas.

DATOS GENERALES
- Universidad El Bosque · Facultad de Ingeniería · Programa de Ingeniería de Sistemas · Curso Proyecto Núcleo 1
- Profesor: Carlos Medina Ayala
- Equipo: Juan Pablo Gómez (Gerente de Proyecto), Cristhian Hernández, Juan Palacios
- Fecha de entrega: 13 de noviembre de 2026

ESTRUCTURA

1. Portada
Título "Sistema de Gestión de Horarios y Matrícula (SGHM)", subtítulo "Proyecto Núcleo 1 – Programa de Ingeniería de Sistemas", nombres del equipo y del profesor, noviembre de 2026.

2. El problema (situación actual)
- Los horarios se arman en hojas de cálculo independientes y desconectadas.
- Cada Comité de Área maneja su propia información; consolidar la carga docente tarda días.
- Los horarios llegan a los docentes por correo o teléfono, a veces sin respetar su disponibilidad, y cambian a última hora.
- La inscripción no muestra cupos en tiempo real ni impide cruces.
- El cierre de grupos se avisa tarde, con un correo genérico y sin alternativas.

3. Contexto institucional
Cifras grandes en tarjetas: 2 sedes (Usaquén y Chía) · 17 edificios · 285 espacios académicos · 149 aulas regulares · 20 aulas de cómputo (solo Usaquén) · 40 laboratorios · 4 auditorios (uso excepcional).

4. Objetivo y alcance
- Objetivo: un sistema web que programe horarios sin cruces de docentes ni aulas, gestione la inscripción y la matrícula, y genere reportes de ocupación y carga docente sobre el inventario real.
- Fuera de alcance (versión 1): nómina docente, gestión financiera de la matrícula, reserva de espacios para eventos e integración con sistemas de terceros.

5. Marco de referencia
Diagrama de 5 bloques con los componentes del sistema de información según el PEP: personas, infraestructura de TI, datos e información, procesos organizacionales y software. Debajo, una línea con las etapas CDIO: Concebir → Diseñar → Implementar → Operar.

6. Actores y roles
Tabla de 3 columnas (Actor · Rol en el sistema · Qué hace):
- Administrador · ADMINISTRADOR · parametriza reglas, periodos, usuarios, docentes, materias y aulas
- Director del Programa · DIRECTOR_PROGRAMA · genera, ajusta y publica horarios; decide el cierre de grupos
- Docente · DOCENTE · registra su disponibilidad y revisa su asignación en borrador
- Estudiante · ESTUDIANTE · consulta la oferta, inscribe materias y descarga su comprobante
- Decano · DECANO · consulta la carga docente frente al presupuesto
- Comité Táctico · sin rol en el sistema · avala la oferta fuera del sistema

7. Levantamiento de requisitos
- Instrumentos: análisis documental, 4 entrevistas (Decano, Vicerrector Administrativo, docente y estudiante), historias de usuario y diagrama de casos de uso.
- Resultado: 55 historias de usuario, 58 requisitos funcionales, 8 restricciones.
- La verificación cruzada detectó vacíos que se corrigieron; por ejemplo, la nueva HU-55 (el docente revisa su asignación antes de publicar).
Recuadro: "[Captura: diagrama de casos de uso]"

8. Los 9 módulos del sistema
Cuadrícula de 9 tarjetas con ícono: Usuarios y autenticación · Docentes y vinculación · Materias y grupos · Aulas y espacios · Generación de horarios · Inscripción y matrícula · Notificaciones y reportes · Parametrización · Auditoría y seguridad.

9. Reglas de negocio clave
- Franja: lunes a viernes de 7:00 a. m. a 10:00 p. m. y sábados de 7:00 a. m. a 1:00 p. m., con almuerzo bloqueado de 12:00 m. a 1:00 p. m.
- Sesiones de 2 horas, de 1 a 4 por semana; se evitan días consecutivos.
- Grupos de máximo 40 estudiantes con 10 % de tolerancia; cierre de grupos con menos de 10 inscritos.
- Sin cruces de docente ni de aula; aula con capacidad y requisitos adecuados.
- Todos los valores son parametrizables.

10. Arquitectura
Diagrama de capas de izquierda a derecha: Cliente web Angular → API REST con JWT (controladores) → Servicios (reglas de negocio) → Repositorios JPA → Base de datos MySQL. Al lado, servidor de correo SMTP. Etiqueta: "Patrón MVC por capas".
Tecnologías en chips: Java 25, Spring Boot 4.1.1, Spring Security, JPA/Hibernate, MySQL, Angular 21, Bootstrap 5, Angular CDK (arrastrar y soltar), OpenPDF y Apache POI, Git y GitHub.

11. Generación automática de horarios
- El problema de horarios es NP-completo: no existe una solución óptima garantizada en tiempo razonable.
- Se eligió una heurística constructiva voraz que programa primero los grupos más difíciles.
- Aplica todas las restricciones duras y trata los días consecutivos como restricción blanda.
- Si no logra programar un grupo, informa el cuello de botella; la decisión final es del Director.
Diagrama de flujo simple: Borrador → Generar propuesta → Ajuste manual (arrastrar y soltar) → Revisión docente → Publicar (horario inmutable). Nota: se conservan las últimas 5 versiones para restaurar.

12. Seguridad y calidad (ISO/IEC 25010)
- JWT con vigencia de 60 minutos y contraseñas cifradas con BCrypt.
- Bloqueo de 15 minutos tras 3 intentos fallidos; recuperación de contraseña con enlace de un solo uso de 30 minutos.
- Permisos por rol en cada endpoint; cada usuario solo ve sus datos.
- Bitácora de auditoría de todos los cambios.
- Mensajes de error que explican la causa exacta del rechazo.

13. La aplicación web
Muestra 4 recuadros de captura con un título corto cada uno:
- "[Captura: Planificador semanal con arrastrar y soltar]"
- "[Captura: Oferta académica e inscripción del estudiante]"
- "[Captura: Publicación del horario]"
- "[Captura: Reporte de carga docente]"
Texto breve: menús según el rol del usuario, selector global de periodo, notificaciones y exportación de reportes a PDF y Excel.

14. Pruebas
Destacado grande: "12 de 12 casos de prueba cumplen".
Tabla resumida con 6 ejemplos (Caso · Regla verificada):
- CP-01 · Bloqueo de cuenta tras 3 intentos fallidos
- CP-03 · Ninguna sesión puede cruzar el almuerzo
- CP-05 · Se respeta la restricción horaria del docente
- CP-07 · No se publica con observaciones pendientes
- CP-09 · Se valida el prerrequisito al inscribir
- CP-12 · La generación informa el cuello de botella
Pie: "Ejecutados desde la interfaz el 19 de octubre de 2026 contra el backend con MySQL."

15. Gestión del proyecto
Línea de tiempo de 8 semanas (21/09 al 13/11/2026) con enfoque Scrum:
- Sprint 0 (21–25 sep): análisis y diseño ◆ Documento de Análisis aprobado (25/09)
- Sprint 1 (28 sep–9 oct): base y datos maestros ◆ Documento de Diseño v1.0 y Sprint Review 1 (09/10)
- Sprint 2 (13–23 oct): motor de horarios ◆ Sprint Review 2 (23/10)
- Sprint 3 (26 oct–6 nov): inscripción y reportes ◆ Sprint Review 3 (06/11)
- Sprint 4 (9–13 nov): pruebas y entrega ◆ Entrega final (13/11)
Dato: 753 horas planeadas y costo planeado de 23.795.000 COP, solo con software gratuito o con licencia educativa.

16. Conclusiones y trabajo futuro
- Contrastar los instrumentos de levantamiento reveló inconsistencias que un solo instrumento habría ocultado.
- La trazabilidad entre historias, requisitos y código respalda que el diseño cumple las especificaciones.
- Verificar es necesario, pero solo la validación con usuarios reales confirma que se construyó el producto correcto.
- Trabajo futuro: mejorar la heurística con búsqueda tabú o algoritmos evolutivos, integrar Talento Humano y Espacios Físicos, e incorporar inicio de sesión único.
Cierre: "Gracias · ¿Preguntas?"
