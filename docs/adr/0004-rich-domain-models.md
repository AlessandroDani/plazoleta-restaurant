# Architectural Decision Record (ADR)

**Título:** Implementación de Modelos de Dominio Ricos vs. Modelos Anémicos  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

Tradicionalmente, en muchas arquitecturas, las entidades de dominio se tratan como simples contenedores de datos (POJOs con getters y setters), dejando toda la lógica de validación y negocio en los servicios (Use Cases). Esto genera "Modelos Anémicos" donde la lógica está dispersa, es difícil de reutilizar y los modelos pueden terminar en estados inválidos si el servicio no aplica las reglas correctamente.

---

## 2. Fuerzas

- **Encapsulamiento:** Las reglas que dependen exclusivamente de los datos de una entidad deben residir dentro de ella.
- **Claridad del Use Case:** Los casos de uso deben enfocarse en la orquestación (flujo) y no en validaciones internas detalladas de cada objeto.
- **Cohesión:** Mantener los datos y el comportamiento que los manipula en el mismo lugar.
- **Integridad:** Asegurar que un objeto de dominio sea capaz de validarse a sí mismo (ej. verificar si un usuario es el dueño de un restaurante).

---

## 3. Decisión

Adoptar el patrón de **Modelo de Dominio Rico**. Las entidades de dominio (como `Restaurant`, `Order`, etc.) no solo contendrán atributos, sino también métodos que ejecuten lógica de negocio y validaciones intrínsecas.

- **Responsabilidad del Modelo:** Validar estados internos, comparar IDs de propiedad (como `validateOwner`) y verificar integridad de listas relacionadas (como `validatePlateList`).
- **Responsabilidad del Use Case:** Orquestar la persistencia, llamar a servicios externos y coordinar múltiples entidades.
- **Manejo de Excepciones:** El modelo lanzará excepciones de dominio específicas (`PlateNotAvailableException`, `UserIsNotOwnerRestaurantException`) que serán capturadas por los adaptadores de infraestructura.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Código más Limpio:** Los Use Cases son más legibles y se centran en el "qué" hace el sistema, mientras que el modelo se centra en el "cómo" se valida la entidad.
- **Robustez:** Es imposible pasar por alto una validación crítica si esta es obligatoria para operar con el objeto.
- **Facilidad de Testing:** Las validaciones de negocio se pueden probar con tests unitarios simples sobre la entidad, sin necesidad de mockear dependencias complejas del caso de uso.

**Negativas:**
- **Crecimiento de la Clase:** Las clases de modelo pueden volverse extensas al incluir lógica además de los campos y el patrón Builder.

---

## 6. Alternativas consideradas

1. **Modelo Anémico:** Mantener las entidades solo con datos. Se descartó porque infla los Use Cases con lógica repetitiva y debilita el encapsulamiento del dominio.

---

## 7. Resultados esperados

- Casos de uso altamente legibles y enfocados en la orquestación.
- Entidades de dominio autosuficientes que garantizan el cumplimiento de las reglas de negocio en todo momento.