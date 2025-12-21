# Architectural Decision Record (ADR)

**Título:** Optimización de Validación de Platos mediante Carga en Lote (Batch Validation)  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

Al crear un pedido, es obligatorio validar que cada plato solicitado exista y pertenezca al restaurante correspondiente.

**Problema:** Inicialmente, el sistema realizaba una consulta individual a la base de datos por cada plato del pedido. En pedidos con múltiples ítems, esto generaba un alto tráfico de red y latencia innecesaria (problema de N+1 consultas), afectando el rendimiento del microservicio.

---

## 2. Fuerzas

- **Rendimiento:** Minimizar el número de conexiones y consultas a la base de datos.
- **Escalabilidad:** El sistema debe manejar pedidos con múltiples platos de forma eficiente.
- **Integridad de Datos:** Garantizar que no se procesen platos de otros restaurantes o inexistentes.
- **Simplicidad de Código:** Mantener una lógica de validación limpia dentro del modelo de dominio.

---

## 3. Decisión

Implementar una estrategia de **Validación en Lote (Batch Validation)** dentro de la entidad `Restaurant`:

- **Consulta Única:** Se realiza una sola consulta a la base de datos para obtener la lista completa de IDs de platos activos de un restaurante específico.
- **Validación en Memoria:** El método `validatePlateList` itera sobre los platos del pedido y verifica su existencia en la lista de IDs obtenida previamente utilizando el método `contains()`.
- **Excepción Unificada:** Si cualquier plato no se encuentra en la lista (ya sea porque no existe o pertenece a otro restaurante), se lanza una excepción única `PlateNotAvailableException`.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Optimización de Recursos:** Se reduce drásticamente el tiempo de respuesta al pasar de N consultas a 1 sola consulta por pedido.
- **Eficiencia del Dominio:** El modelo rico (ADR 0004) ahora procesa la validación de forma más rápida utilizando estructuras de datos en memoria.

**Negativas:**
- **Pérdida de Granularidad en el Error:** Al usar una excepción general, el sistema no le indica al usuario exactamente cuál de los platos es el que falla o si el motivo es inexistencia o pertenencia a otro comercio.
- **Consumo de Memoria:** Se carga una lista de IDs en memoria, aunque dado que los menús de los restaurantes suelen ser limitados, el impacto es despreciable.

---

## 6. Alternativas consideradas

1. **Consultas Individuales (Original):** Se descartó por ser ineficiente y no escalar ante pedidos grandes.
2. **Excepciones Detalladas por Ítem:** Se consideró mapear cada error individualmente, pero se priorizó el patrón **Fail-Fast** (fallar rápido ante la primera inconsistencia) para proteger la integridad del sistema con menor complejidad técnica.

---

## 7. Resultados esperados

- Mejora medible en los tiempos de respuesta del endpoint de creación de pedidos.
- Reducción de la carga de lectura en la base de datos relacional.