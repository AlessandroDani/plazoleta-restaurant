# Architectural Decision Record (ADR)

**Título:** Uso Selectivo del Patrón Builder basado en Complejidad de Modelos  
**Versión:** MADR v2.1  
**Estado:** Aprobado  
**Fecha:** 2025-12-21

---

## 1. Contexto y Problema

A medida que el dominio del sistema creció, entidades como `Restaurant`, `Plate` (Plato), `User` y `Order` comenzaron a acumular una cantidad considerable de atributos (8 o más). El uso de constructores tradicionales o múltiples constructores sobrecargados dificultaba la legibilidad, aumentaba el riesgo de error al pasar parámetros del mismo tipo (ej. múltiples Strings) y complicaba el mantenimiento del código.

---

## 2. Fuerzas

- **Legibilidad:** El código debe ser fácil de leer y entender al instanciar objetos complejos.
- **Simplicidad (KISS):** No se deben introducir patrones de diseño complejos en clases que no los requieren.
- **Evitar Sobre-ingeniería (YAGNI):** No implementar estructuras pensando en un crecimiento futuro que no es seguro o necesario actualmente.
- **Mantenibilidad:** La creación de objetos debe ser robusta ante el cambio de orden o cantidad de atributos.

---

## 3. Decisión

Implementar el **Patrón Builder** de manera selectiva bajo los siguientes criterios:

- **Aplicación del Patrón:** Se utilizará exclusivamente en entidades con alta densidad de atributos (aprox. 6-8 o más) o donde la construcción del objeto sea propensa a errores por parámetros ambiguos. Esto incluye: `User`, `Restaurant`, `Plate` y `Order`.
- **Exclusión del Patrón:** Se mantendrán constructores estándar para modelos pequeños (2 a 5 atributos).
---

## 4. Estado

✅ **Aprobado e Implementado.**

---

## 5. Consecuencias

**Positivas:**
- **Claridad:** La construcción de objetos complejos es ahora semántica y legible.
- **Eficiencia de Desarrollo:** Se evita la complejidad innecesaria en modelos simples, manteniendo el código base ligero.
- **Flexibilidad:** Permite la creación de objetos con atributos opcionales de forma más limpia.

**Negativas:**
- **Falta de Uniformidad:** El proyecto presenta dos formas distintas de instanciar objetos (Constructores vs Builders), lo que podría confundir a desarrolladores externos que esperen un estándar único.

---

## 6. Alternativas consideradas

1. **Aplicar Builder a todos los modelos:** Se descartó por el principio **YAGNI**. Generar builders para clases de 2 o 3 atributos añade ruido visual y archivos/clases internas innecesarias sin aportar un beneficio real en legibilidad.
2. **Uso exclusivo de Setters:** Se descartó para evitar objetos mutables y estados inconsistentes durante la construcción de la entidad.

---

## 7. Resultados esperados

- Reducción de errores en la instanciación de entidades críticas.
- Código fuente más limpio y alineado con la complejidad real de cada módulo.
- Cumplimiento de los principios KISS y YAGNI en toda la capa de dominio.