# Architectural Decision Record (ADR)

**Título:** Soporte para Cadenas de Restaurantes (Relación Uno a Muchos entre Propietario y Restaurante)  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

El sistema debe permitir que la plataforma escale para grandes marcas o franquicias. Inicialmente, se podría pensar en una relación 1:1, donde un propietario solo gestiona un establecimiento. Sin embargo, el requerimiento de negocio exige soportar el concepto de **cadena de restaurantes**.

**Problema:** ¿Cómo y dónde modelar la capacidad de que un solo usuario con rol `OWNER` sea el titular de múltiples sedes o establecimientos?

---

## 2. Fuerzas

- **Escalabilidad de Negocio:** Permitir que un inversionista o dueño gestione toda su franquicia desde una sola cuenta.
- **Aislamiento de Dominios:** El microservicio de Usuarios debe permanecer agnóstico a las entidades de negocio (como Restaurantes).
- **Flexibilidad:** El diseño debe permitir agregar nuevas sedes a un dueño existente sin modificar su perfil de usuario.

---

## 3. Decisión

Implementar una relación de **Uno a Muchos (1:N)** entre el Propietario y el Restaurante dentro del **Microservicio de Plazoleta**:

- **Modelado:** La tabla `restaurantes` en la base de datos de Plazoleta contendrá una columna `id_propietario` que actuará como llave foránea lógica hacia el ID del usuario.
- **Ubicación de la Regla:** La validación de esta relación se gestionará en Plazoleta. Cuando se cree un nuevo restaurante, se verificará que el `id_propietario` provisto tenga el rol adecuado (consultando a Usuarios), pero la persistencia permitirá múltiples registros de restaurantes para el mismo ID de dueño.
- **Impacto en el Dominio:** La entidad `Restaurant` (ADR 0008) mantiene su atributo `idOwner`, permitiendo que el repositorio de Plazoleta recupere todos los restaurantes asociados a un ID específico.

---

## 4. Estado

✅ **Aprobado.**

---

## 5. Consecuencias

**Positivas:**
- **Soporte de Franquicias:** El sistema está listo para manejar cadenas de restaurantes de forma nativa.
- **Desacoplamiento:** El microservicio de Usuarios no se ve afectado; no necesita saber cuántos restaurantes posee una persona.
- **Facilidad de Reportes:** Permite filtrar pedidos o ventas por "dueño" agrupando todas sus sedes fácilmente.

**Negativas:**
- **Complejidad en Validaciones:** Al crear un restaurante, Plazoleta debe asegurar que el ID del propietario realmente exista y sea válido, aumentando ligeramente la dependencia de la comunicación inter-servicios.

---

## 6. Alternativas consideradas

1. **Modelar la relación en el microservicio de Usuarios:** Se descartó porque violaría el principio de responsabilidad única; el microservicio de Usuarios terminaría con tablas o campos específicos de "negocio de comidas".
2. **Restricción 1:1:** Se descartó por ser un limitante crítico para el crecimiento de la plataforma.

---

## 7. Resultados esperados

- Un usuario con rol `OWNER` podrá aparecer como dueño en N registros de la tabla `restaurantes`.
- La arquitectura soportará de forma natural la expansión de marcas comerciales a través de múltiples sedes.