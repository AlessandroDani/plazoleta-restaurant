# Architectural Decision Record (ADR)

**Título:** Manejo Global de Excepciones y Mapeo de Errores de Dominio  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

Al implementar **Arquitectura Hexagonal** con un **Modelo de Dominio Rico**, las entidades y casos de uso lanzan excepciones específicas de negocio (ej. `PlateNotAvailableException`). Si no se gestionan de forma centralizada, cada controlador (`Adapter`) tendría que usar bloques `try-catch` repetitivos, lo que ensucia el código de infraestructura y genera respuestas HTTP inconsistentes.

---

## 2. Fuerzas

- **Separación de Concernimientos:** Los controladores no deben conocer los detalles de cómo se procesa un error, solo deben entregar la respuesta.
- **Consistencia:** Todas las respuestas de error de la API deben tener la misma estructura (ej. mensaje, código de error, timestamp).
- **Mantenibilidad:** Centralizar la lógica de errores facilita agregar o modificar mensajes sin tocar múltiples clases.
- **Experiencia de Usuario (DX):** Proporcionar mensajes de error claros y códigos de estado HTTP precisos (400, 403, 404, etc.).

---

## 3. Decisión

Implementar un **Global Exception Handler** utilizando la anotación `@ControllerAdvice` de Spring Boot en la capa de infraestructura.

- **Mapeo de Excepciones:** Crear un método específico (`@ExceptionHandler`) por cada excepción de dominio o grupo de excepciones.
- **Traducción de Errores:** El manejador capturará las excepciones lanzadas desde el **Dominio** o **Aplicación** y las traducirá a un cuerpo de respuesta estandarizado y un código de estado HTTP adecuado.
- **Estructura de Respuesta:** Se define un objeto `ExceptionResponse` que contenga los detalles del error para el cliente.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Controladores Limpios:** Los controladores se reducen a una sola línea de ejecución (llamar al caso de uso) sin preocuparse por los fallos.
- **Estandarización:** El cliente de la API (Frontend o Postman) siempre recibe el mismo formato de error, independientemente de qué microservicio falle.
- **Desacoplamiento:** El Dominio lanza excepciones puras de Java y la Infraestructura decide qué código HTTP (403 Forbidden, 409 Conflict) le corresponde.

**Negativas:**
- **Complejidad Inicial:** Requiere la creación de una clase adicional y un catálogo de excepciones bien definido.

---

## 6. Alternativas consideradas

1. **Manejo local en cada Controlador:** Se descartó por la alta duplicación de código y el riesgo de enviar códigos de estado inconsistentes para el mismo error.
2. **Uso de ResponseStatusException:** Se descartó porque acopla las excepciones de negocio a clases de Spring, rompiendo la independencia tecnológica del dominio.

---

## 7. Resultados esperados

- Reducción del 100% de bloques `try-catch` en los controladores REST.
- Proporcionar una interfaz de error predecible y profesional para todos los microservicios del ecosistema Power-up.