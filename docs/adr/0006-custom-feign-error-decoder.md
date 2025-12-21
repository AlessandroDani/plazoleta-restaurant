# Architectural Decision Record (ADR)

**Título:** Manejo de Errores en Comunicación Inter-servicios mediante Feign ErrorDecoder  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

En una arquitectura de microservicios, la comunicación entre componentes (ej. el microservicio de Plazoleta consultando al de Usuarios via Feign Client) puede fallar por múltiples razones: datos inválidos, falta de permisos o indisponibilidad del servicio. Por defecto, OpenFeign encapsula cualquier respuesta de error (4xx o 5xx) en una excepción genérica `FeignException`.

**Problema:** Al recibir una `FeignException`, el microservicio que hace la llamada pierde el contexto semántico del error original, dificultando la toma de decisiones lógica y provocando que la API responda con errores 500 genéricos ante fallos que son, en realidad, de validación (400) o permisos (403).

---

## 2. Fuerzas

- **Propagación de Errores:** La razón del fallo en el servicio B debe ser clara para el servicio A.
- **Resiliencia:** El sistema debe reaccionar de forma específica según el tipo de error externo.
- **Abstracción:** El resto de la aplicación no debe lidiar con excepciones propias de la librería Feign, sino con excepciones de infraestructura o dominio propias.
- **Mantenibilidad:** Centralizar la lógica de interpretación de respuestas HTTP externas.

---

## 3. Decisión

Implementar un **`CustomErrorDecoder`** para todos los clientes Feign del ecosistema.

- **Interpretación de Status:** El decodificador interceptará cada respuesta con estado distinto de 2xx.
- **Mapeo Semántico:** Se traducirán los códigos de estado HTTP externos a excepciones personalizadas del proyecto:
    - `400 BAD_REQUEST` -> `InvalidDataException`
    - `401 UNAUTHORIZED` -> `UserAuthenticationException`
    - `403 FORBIDDEN` -> `ActionForbiddenException`
    - `404 NOT_FOUND` -> `ResourceNotFoundException`
    - `5xx` -> `ExternalServiceFailureException`
- **Integración:** Estas excepciones serán capturadas por el `GlobalExceptionHandler` para retornar una respuesta coherente al cliente original.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Transparencia:** El flujo de error se mantiene claro a través de toda la cadena de microservicios.
- **Mejor Diagnóstico:** Facilita la identificación de si un error es por culpa del cliente (4xx) o del sistema (5xx).
- **Desacoplamiento:** La lógica de negocio no se contamina con excepciones técnicas de la librería de cliente HTTP.

**Negativas:**
- **Acoplamiento de Errores:** Requiere que los microservicios compartan una semántica de errores similar para que el mapeo sea efectivo.

---

## 6. Alternativas consideradas

1. **Bloques Try-Catch en cada Cliente Feign:** Se descartó por la alta duplicación de código y por ser propenso a errores al olvidar manejar ciertos estados.
2. **Ignorar Errores (Default Feign):** Se descartó porque oscurece la causa raíz de los fallos, afectando negativamente la experiencia del usuario y el monitoreo.

---

## 7. Resultados esperados

- Mapeo consistente de errores inter-microservicios.
- Mejora en la precisión de los códigos de respuesta HTTP entregados al cliente final.