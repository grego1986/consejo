# Sistema de Gestión Parlamentaria
Sistema web desarrollado para la digitalización y gestión de expedientes en un entorno municipal, reemplazando procesos manuales en papel por un flujo digital trazable.

---

## 🧠 Descripción

La aplicación permite gestionar expedientes administrativos, digitalizar notas y realizar el seguimiento de su estado a lo largo del tiempo.

Incluye control de acceso basado en roles, permitiendo que distintos tipos de usuarios interactúen con el sistema según sus permisos.

Este sistema fue implementado en un entorno real, contribuyendo a mejorar la organización y eficiencia de los procesos administrativos.

---

## 🚀 Tecnologías utilizadas

- **Backend:** Java (Spring Boot)
- **Base de datos:** MySQL
- **Frontend:** Thymeleaf, HTML, CSS, Bootstrap
- **Contenerización:** Docker
- **Orquestación:** Docker Compose

---

## 🐳 Arquitectura

El sistema está compuesto por dos servicios principales:

- **Aplicación (Spring Boot):**
  - Maneja la lógica de negocio
  - Expone la interfaz web
- **Base de datos (MySQL):**
  - Persistencia de datos
  - Uso de volumen para mantener la información

Ambos servicios están orquestados mediante **Docker Compose**, permitiendo levantar todo el entorno de forma simple y reproducible.

Además, el sistema implementa una espera activa al iniciar para asegurar que la base de datos esté disponible antes de que la aplicación intente conectarse.

---

## ⚙️ Cómo ejecutar el proyecto

### 🔧 Requisitos

- Docker
- Docker Compose

### ▶️ Pasos

```bash
# Clonar el repositorio
git clone https://github.com/grego1986/consejo.git

# Ingresar al proyecto
cd consejo

# Levantar el entorno
docker-compose up --build

## 📸 Capturas

Ingreso de notas
<img width="1909" height="1027" alt="sgp-ingresonota" src="https://github.com/user-attachments/assets/fe968b44-a59b-4934-bab5-0796aa431bc4" />

Búsqueda de expedientes
<img width="1913" height="1025" alt="sgp-busqueda" src="https://github.com/user-attachments/assets/5eee78ec-9ebf-449a-a1bd-20d1c0ce86b9" />

Vista del expediente
<img width="1915" height="1015" alt="sgp-ver" src="https://github.com/user-attachments/assets/c9382e36-c700-47a4-8373-b5338e8c4a0b" />

## 📁 Estructura del proyecto

El sistema sigue una arquitectura basada en capas:

controller → Manejo de solicitudes HTTP
service → Lógica de negocio
repository → Acceso a datos
model → Entidades del sistema

## 🗃️ Modelo de datos

El sistema gestiona entidades principales como:

Expedientes
Usuarios
Roles

Estas entidades se relacionan para permitir un flujo ordenado y controlado de los procesos administrativos.

##📈 Impacto

Reducción significativa del uso de papel
Mejora en la trazabilidad de los expedientes
Digitalización de procesos administrativos
Implementación en entorno real

## 🚧 Mejoras futuras

API REST para integración con otros sistemas
Implementación de autenticación más robusta
Mejoras en la interfaz de usuario
Despliegue en entorno cloud

## 👨‍💻 Autor

José Luis Gregoretti

LinkedIn: https://linkedin.com/in/GregoDev
GitHub: https://github.com/grego1986


