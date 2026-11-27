# Parcial 3: Sistema de Notificaciones Multicanal

Proyecto desarrollado en **Java** para la materia **Ingeniería de Software II**, enfocado en la aplicación práctica de **Patrones de Diseño** y principios **SOLID**.

---

## 👥 Integrantes
- **Andrés Mambuscay**
- **Andrés Fonseca**
- **David García**

---

## 🏛️ Patrones de Diseño Aplicados

| Patrón | Categoría | Clases en el Proyecto | Problema que Resuelve |
|---|---|---|---|
| **Adapter** | **Estructural** | `EmailAdapter`, `SmsAdapter`, `WhatsAppAdapter` | Unifica las APIs y formatos dispares de cada canal bajo la interfaz `CanalNotificacion`. |
| **Strategy** | **Comportamiento** | `EstrategiaUrgente`, `EstrategiaNormal`, `EstrategiaConReintento` | Permite alternar dinámicamente las políticas de entrega (todos los canales, canal único o failover) sin `if-else` acoplados. |
| **Facade** | **Estructural** | `NotificadorFacade` | Expone una interfaz única y sencilla para la Consola y la GUI Swing, ocultando la complejidad interna del sistema. |

---

## 📁 Distribución de Módulos

- **Módulo 1 (Andrés Mambuscay — Canales y Adapter):**
  - `com.notificaciones.externo`: `ServicioEmailExterno`, `ServicioSMSExterno`, `ServicioWhatsAppExterno`.
  - `com.notificaciones.adapter`: `CanalNotificacion`, `EmailAdapter`, `SmsAdapter`, `WhatsAppAdapter`.
  - `com.notificaciones.model`: `TipoCanal`.

- **Módulo 2 (Andrés Fonseca — Reglas de Envío y Strategy):**
  - `com.notificaciones.strategy`: `EstrategiaEnvio`, `ResultadoDespacho`, `EstrategiaUrgente`, `EstrategiaNormal`, `EstrategiaConReintento`.
  - `com.notificaciones.model`: `EstadoEnvio`.

- **Módulo 3 (David García — Fachada, Plantillas, Interfaces y Pruebas):**
  - `com.notificaciones.facade`: `NotificadorFacade`.
  - `com.notificaciones.model`: `Usuario`, `Mensaje`, `RegistroNotificacion`.
  - `com.notificaciones.ui`: `MenuConsola`, `VentanaPrincipal` (Swing).
  - `com.notificaciones.test`: `PruebasPatronesTest`.
  - `com.notificaciones.Main`.

---

## 🚀 Compilación y Ejecución

### 1. Compilar todo el proyecto:
```bash
javac -d bin src/com/notificaciones/model/*.java src/com/notificaciones/externo/*.java src/com/notificaciones/adapter/*.java src/com/notificaciones/strategy/*.java src/com/notificaciones/facade/*.java src/com/notificaciones/ui/*.java src/com/notificaciones/Main.java src/com/notificaciones/test/*.java
```

### 2. Ejecutar la aplicación:
- **Modo interactivo:**
  ```bash
  java -cp bin com.notificaciones.Main
  ```
- **Versión por Consola:**
  ```bash
  java -cp bin com.notificaciones.Main --consola
  ```
- **Interfaz Gráfica (Java Swing):**
  ```bash
  java -cp bin com.notificaciones.Main --gui
  ```

### 3. Ejecutar Pruebas Unitarias:
```bash
java -cp bin com.notificaciones.test.PruebasPatronesTest
```
