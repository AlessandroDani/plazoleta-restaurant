# Architectural Decision Record (ADR)

**Título:** Modelado de Orden como Agregado con Lista de Platos  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

En el diseño relacional de la base de datos, la información de un pedido está fragmentada en las tablas `PEDIDOS` y `PEDIDOS_PLATOS` (tabla asociativa). Originalmente, el modelo de dominio reflejaba esta estructura plana.

**Problema:** Al procesar un pedido en el Use Case o el Dominio, se requería realizar múltiples consultas o manejar objetos separados para conocer el contenido del pedido, lo que complicaba la lógica de negocio y aumentaba la carga cognitiva del desarrollador.

---

## 2. Fuerzas

- **Consistencia:** Un pedido debe ser tratado como una unidad atómica de información.
- **Simplicidad de Uso:** Los casos de uso deben poder acceder a los detalles del pedido (platos y cantidades) directamente desde el objeto principal.
- **Eficiencia en Capa de Aplicación:** Reducir la complejidad de orquestación al recuperar datos de infraestructura.
- **Patrón Aggregate (DDD):** Agrupar objetos relacionados que se tratan como una unidad para cambios de datos.

---

## 3. Decisión

Redefinir la clase de dominio `Order` para que funcione como un **Root Aggregate**, incluyendo una lista de objetos `OrderPlate` como atributo propio.

- **Composición:** La entidad `Order` ahora posee una `List<OrderPlate> plates`.
- **Carga de Datos:** El adaptador de persistencia (JPA/SQL) se encarga de mapear la relación de la tabla asociativa `PEDIDOS_PLATOS` para poblar esta lista al recuperar un pedido.
- **Encapsulamiento:** Toda validación que involucre la suma de platos o verificación de disponibilidad se realizará dentro del objeto `Order`, aprovechando que ya posee la información de sus platos.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Navegabilidad mejorada:** Es mucho más natural acceder a `order.getPlates()` que realizar una búsqueda externa por ID de pedido.
- **Lógica de Negocio Centralizada:** Permite implementar métodos como `calculateTotal()` o `validateOrderRules()` directamente en el modelo rico (ADR 0008).
- **Código más Limpio:** Se eliminan múltiples parámetros innecesarios en los métodos de los Use Cases.

**Negativas:**
- **Acoplamiento Interno:** La entidad `Order` ahora depende de la existencia de `OrderPlate`.
- **Carga de Memoria:** Al traer un pedido, siempre se traen sus platos (aunque esto es deseable en el 99% de los casos de uso de este sistema).

---

## 6. Alternativas consideradas

1. **Mantener modelos planos:** Manejar `Order` y buscar los platos mediante un servicio de repositorio independiente cada vez. Se descartó por ser ineficiente y propenso a errores de consistencia.

---

## 7. Resultados esperados

- Facilitar la implementación de la lógica de "Cierre de Pedido" y "Validación de Pin de Seguridad".
- Reducir el número de métodos en los puertos de salida (Gateways/Repositories) al recuperar objetos completos en una sola operación de dominio.