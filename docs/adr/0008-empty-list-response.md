# Architectural Decision Record (ADR)

**Título:** Respuesta de Colecciones Vacías en Consultas Exitosas  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

Cuando un cliente realiza una petición GET para obtener una lista de recursos (ej. platos de un restaurante, pedidos de un cliente) y no existen registros en la base de datos que coincidan con los criterios, surge la duda de qué debe retornar el servidor. Las opciones comunes son un error `404 Not Found`, un valor `null` o una lista vacía `[]`.

**Problema:** El uso de errores o valores nulos para representar la ausencia de datos en una colección obliga a los clientes a implementar lógica de manejo de excepciones o validaciones de nulidad adicionales, lo que complica el consumo de la API.

---

## 2. Fuerzas

- **Semántica HTTP:** Un código `200 OK` indica que la petición se procesó correctamente. Si la búsqueda terminó sin errores pero no hubo coincidencias, la petición sigue siendo válida.
- **Robustez del Cliente:** Los clientes suelen iterar sobre las listas. Una lista vacía `[]` permite que los ciclos (for/map) funcionen sin fallar, mientras que un `null` o un error `404` requiere un manejo especial.
- **Consistencia:** Mantener un comportamiento predecible en todos los endpoints de tipo "lista" del ecosistema de microservicios.

---

## 3. Decisión

Adoptar el estándar de retornar una **lista vacía `[]` con un código de estado `200 OK`** cuando una consulta de colección no encuentra resultados.

- **No lanzar excepciones:** No se lanzarán excepciones de tipo `ResourceNotFoundException` en métodos que retornen listas.
- **Evitar Nulos:** Los adaptadores de persistencia y los casos de uso deben asegurar que siempre se retorne una instancia de lista (aunque sea de tamaño 0) y nunca un valor nulo.
- **Distinción:** El error `404 Not Found` se reservará exclusivamente para consultas de recursos individuales (por ID) que no existan.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Mejor DX (Developer Experience):** Los desarrolladores frontend pueden mapear la respuesta directamente a la interfaz sin bloques try-catch o validaciones `if(data != null)`.
- **Menos errores en el cliente:** Se eliminan los errores de tipo "Cannot read property 'map' of null".
- **Semántica Clara:** Se diferencia claramente entre "El recurso no existe" (404) y "La búsqueda no arrojó resultados" (200 + []).

**Negativas:**
- **Ambigüedad:** Como bien se identificó, no es explícito para el consumidor si la consulta falló silenciosamente o si realmente no hay información. El cliente debe confiar en que un 200 garantiza que la lógica de búsqueda se ejecutó correctamente.
---

## 6. Alternativas consideradas

1. **Retornar 404 Not Found:** Se descartó porque el 404 implica que el *endpoint* o el *recurso específico* no existe, no que la colección esté vacía.
2. **Retornar null:** Se descartó por ser una mala práctica que traslada la responsabilidad de validación al cliente y aumenta el riesgo de errores en tiempo de ejecución.
3. **Retornar 204 No Content:** Se consideró, pero muchos clientes de API modernos manejan mejor un cuerpo `[]` que un cuerpo totalmente vacío para mantener el tipo de dato (Array).
---

## 7. Resultados esperados

- Unificación del comportamiento de todas las consultas de lista en los microservicios de Usuarios, Plazoleta y Trazabilidad.
- Simplificación del código en los clientes que consumen la API.