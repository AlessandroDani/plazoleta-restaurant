# Architectural Decision Record (ADR)

**Título:** Creación de Tabla de Asociación Restaurante-Empleado en Microservicio Plazoleta  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

En el sistema actual, los usuarios con el rol `ROLE_EMPLEADO` existen de forma independiente en el microservicio de Usuarios. Sin embargo, al gestionar pedidos en el microservicio de **Plazoleta**, surgió la necesidad de validar que un empleado pertenezca específicamente a la sede donde se realizó el pedido para permitir cambios de estado (ej. de 'En preparación' a 'Listo').

**Problema principal:** Falta de vinculación técnica entre empleados y restaurantes, lo que impide validar la autorización a nivel de negocio para la gestión de pedidos y compromete la integridad de los procesos de la plazoleta.

---

## 2. Fuerzas

- **Seguridad y Autorización:** Solo empleados vinculados a un restaurante específico deben gestionar sus pedidos.
- **Aislamiento de Microservicios:** El microservicio de Usuarios debe limitarse a la gestión de identidad (RBAC) y no conocer la lógica operativa de los restaurantes.
- **Integridad de Reglas de Negocio:** Un empleado solo puede estar asignado a un único restaurante simultáneamente.
- **Mantenibilidad:** Evitar dependencias cíclicas o consultas complejas entre microservicios para validaciones operativas rápidas.

---

## 3. Decisión

Adoptar la creación de una tabla de asociación denominada `restaurante_empleado` dentro del esquema de base de datos del **Microservicio de Plazoleta**, bajo los siguientes principios:

- **Almacenamiento:** Guardar la relación entre el `restaurante_id` y el `usuario_id` (ID externo proveniente del microservicio de usuarios).
- **Ubicación de la Lógica:** Se decide colocar esta lógica en Plazoleta porque la asignación de personal a sedes es una regla puramente operativa del negocio de restaurantes, manteniendo el microservicio de Usuarios enfocado únicamente en autenticación y creación de perfiles.
- **Validación de Flujo:** El sistema consultará esta tabla internamente en Plazoleta para verificar la pertenencia del empleado antes de procesar cambios de estado en los pedidos.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:** - **Desacoplamiento:** El microservicio de Usuarios permanece agnóstico a la estructura organizacional de los restaurantes.
- **Seguridad Reforzada:** Se previene que empleados de una sede manipulen pedidos de otra.
- **Escalabilidad:** Permite que la lógica de asignación de personal crezca (ej. historial de sedes) sin afectar el sistema de identidad global.

**Negativas:** - **Consistencia Eventual:** El ID del usuario es una referencia externa; si un usuario es eliminado en el microservicio de Usuarios, se debe manejar la limpieza o integridad referencial lógica en Plazoleta.
- **Consulta adicional:** Se añade un paso de verificación en la base de datos local antes de procesar acciones de pedidos.

---

## 6. Alternativas consideradas

1. **Agregar `restaurante_id` en la tabla de Usuarios** - Se descartó porque viola la responsabilidad única del microservicio de Usuarios y obligaría a modificar el esquema de usuarios por reglas específicas de la lógica de restaurantes.

2. **Validación por Claims en el JWT** - Se descartó porque los tokens son estáticos hasta su expiración. Si un empleado es movido de restaurante, el JWT seguiría siendo válido para la sede anterior hasta que expire.

---

## 7. Resultados esperados

- Garantizar que el 100% de los cambios de estado de pedidos sean realizados por personal autorizado de la sede correspondiente.
- Control total sobre la restricción de negocio de "un empleado por restaurante" directamente en la persistencia.