# Architectural Decision Record (ADR)

**Título:** Modelos de Dominio Agnósticos a Librerías Externas (Sin Lombok)  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

En la Arquitectura Hexagonal, la capa de **Dominio** debe ser la más estable y pura del sistema. Introducir librerías externas como Project Lombok en las entidades de dominio crea una dependencia directa del código de negocio con una herramienta de terceros. Si la librería dejara de mantenerse, presentara bugs en el procesamiento de anotaciones o si se deseara migrar a otro lenguaje/entorno, el núcleo del sistema se vería comprometido.

---

## 2. Fuerzas

- **Independencia Tecnológica:** El dominio no debe depender de frameworks o librerías de infraestructura.
- **Portabilidad:** El código debe ser Java puro (POJO) para facilitar migraciones o ejecuciones en entornos restringidos.
- **Transparencia:** El comportamiento de los objetos (constructores, getters, setters) debe ser explícito y visible sin necesidad de plugins en el IDE o pre-procesadores de código.
- **Aislamiento:** Evitar el "acoplamiento de compilación" que introducen librerías que manipulan el bytecode.

---

## 3. Decisión

Se prohíbe el uso de **Lombok** u otras librerías de generación de código dentro de la capa de **Domain**. 

- **Implementación:** Los constructores, métodos de acceso (Getters/Setters) y el patrón Builder (donde aplique según el ADR 0002) se escribirán en Java puro de forma manual o generada por el IDE.
- **Excepción:** Se permite el uso de Lombok en las capas de **Infrastructure** y **Application** (ej. DTOs, Adaptadores, Controladores) para reducir el boilerplate donde la pureza arquitectónica no es el objetivo principal.

---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Dominio Puro:** El núcleo del negocio es 100% Java estándar, facilitando su comprensión sin herramientas externas.
- **Seguridad a Largo Plazo:** No hay riesgo de ruptura por actualizaciones de librerías en la capa más crítica.
- **Depuración:** Es más fácil debugear y trazar el flujo de datos cuando los métodos son explícitos en el código fuente.

**Negativas:**
- **Boilerplate:** Incremento significativo en la cantidad de líneas de código manuales (constructores, métodos).
- **Esfuerzo Manual:** Los cambios en los atributos requieren actualizar manualmente constructores y métodos asociados.

---

## 6. Alternativas consideradas

1. **Uso de Java Records:** Considerado para modelos inmutables, pero se descartó si se requiere compatibilidad estricta con versiones antiguas de Java o si se necesita herencia específica que los Records no permiten.
2. **Uso de Lombok en todo el proyecto:** Se descartó para proteger la integridad de la capa de Dominio según los principios de Clean Architecture.

---

## 7. Resultados esperados

- Un núcleo de negocio totalmente desacoplado de dependencias externas.
- Mayor facilidad para realizar pruebas de arquitectura que validen que el dominio no importa nada fuera de su paquete.