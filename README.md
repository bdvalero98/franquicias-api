# 🏪 Franquicias API

API desarrollada en **Java 21** con **Spring Boot WebFlux** para la gestión de franquicias, sucursales y productos. Implementa una arquitectura reactiva, empaquetada con Docker y lista para ejecutarse localmente.

---

## 📋 Descripción de la prueba técnica

Construcción de una API para manejar una lista de franquicias.  
- Una **franquicia** contiene un **nombre** y un listado de **sucursales**.  
- Cada **sucursal** tiene un **nombre** y una lista de **productos**.  
- Cada **producto** tiene un **nombre** y una **cantidad de stock**.

---

## ✅ Criterios cumplidos

| Criterio                                                         | Cumplido |
|------------------------------------------------------------------|----------|
| Proyecto desarrollado en Spring Boot                             | ✅       |
| Endpoint para agregar franquicia                                 | ✅       |
| Endpoint para agregar sucursal a franquicia                      | ✅       |
| Endpoint para agregar producto a sucursal                        | ✅       |
| Endpoint para eliminar producto de una sucursal                  | ✅       |
| Endpoint para modificar el stock de un producto                  | ✅       |
| Endpoint para ver el producto con más stock por sucursal         | ✅       |
| Uso de MongoDB como sistema de persistencia                      | ✅       |
| Aplicación empaquetada con Docker                                | ✅       |
| Uso de programación funcional y reactiva (Spring WebFlux)        | ✅       |
| Endpoint para actualizar nombre de franquicia                    | ✅       |
| Endpoint para actualizar nombre de sucursal                      | ✅       |
| Endpoint para actualizar nombre de producto                      | ✅       |
| Infraestructura como código (Terraform) para despliegue en AWS   | ✅       |
| Despliegue completo en la nube                                   | ❌       |

---

## 🧰 Tecnologías utilizadas

- Java 21
- Spring Boot 3 + WebFlux
- MongoDB 6
- Gradle
- Docker & Docker Compose
- Terraform (infraestructura lista, pendiente despliegue en la nube)

---

## ⚙️ Requisitos

- Docker y Docker Compose
- Git
- JDK 21 (opcional si no usas Docker)

---

## 🔧 Configuración local

### 1. Clona el repositorio

```bash
git clone https://github.com/tu-usuario/franquicias-api.git
cd franquicias-api
```

### 2. Crea el archivo `.env`

```env
MONGO_INITDB_ROOT_USERNAME=admin
MONGO_INITDB_ROOT_PASSWORD=admin123
MONGO_DB_NAME=franquicias
```

### 3. Levanta los servicios con Docker

```bash
docker-compose up --build
```

Esto iniciará:
- MongoDB en `localhost:27017`
- La API en `localhost:8080`

---

## 🧪 Endpoints principales

📍 **Base URL**: `http://localhost:8080`

### Franquicia
- `POST /api/franquicias`
- `PATCH /api/franquicias/{franquiciaId}/actualizar-nombre?nuevoNombre={NuevoNombreFranquicia}`


### Sucursal
- `POST /api/franquicias/{franquiciaId}/sucursales?nombreSucursal={sucursalNombre}`
- `PATCH /api/franquicias/{franquiciaId}/sucursales/actualizar-nombre?nombreActual={nombreActual}&nuevoNombre={nuevoNombre}`

### Producto
- `POST /api/franquicias/{franquiciaId}/sucursales/{sucursal nombre}/productos?nombreProducto={productoNombre}&stock={stock}`
- `DELETE /api/franquicias/{franquiciaId}/sucursales/{sucursal nombre}/productos?nombreProducto={productoNombre}`
- `PATCH /api/franquicias/{franquiciaId}/sucursales/{sucursal nombre}/productos/{producto nombre}?stock={nuevoStock}`
- `GET /api/franquicias/{franquiciaId}//productos-max-stock`
- `PATCH /api/franquicias/{franquiciaId}/sucursales/{nombreSucursal}/productos/actualizar-nombre?nombreActual={nombreActual}&nuevoNombre={nuevoNombre}`
---

## 🧪 Pruebas
Se incluyeron pruebas unitarias para validar la lógica de negocio de las franquicias, sucursales y productos.
```bash
./gradlew test
```
Estas pruebas aseguran los principales flujos: creación, actualización, eliminación y búsquedas dentro de la lógica reactiva.

---

## 📬 Colección Postman

Puedes probar los endpoints importando la colección Postman incluida en el repositorio:

📁 `postman/API_Franquicias_Collection.postman_collection.json`

### Instrucciones:

1. Abre **Postman**.
2. Haz clic en **Import**.
3. Selecciona **Upload Files**.
4. Carga el archivo `API_Franquicias_Collection.postman_collection.json`.

Esto cargará todos los endpoints configurados para que puedas probar la API localmente de forma inmediata.

---

## 🧼 Apagar los servicios

```bash
docker-compose down
```

---

## 📤 Publicar en Docker Hub (opcional)

```bash
# Login
docker login

# Build y push
docker build -t TU_USUARIO_DOCKER/franquicias-api .
docker push TU_USUARIO_DOCKER/franquicias-api
```

---

## 📦 Estado del despliegue en la nube

Se ha preparado toda la infraestructura como código en Terraform (bucket S3, ECR, EC2, RDS), pero aún **no se ha desplegado completamente en la nube** por temas de alcance temporal. Se puede completar en pocos pasos.

---


## 👨‍💻 Autor

**Brayan Danilo Valero Salazar**  
Senior Java Backend Developer  
📧 brayan.valero10@gmail.com
🌐 GitHub: [@bdvalero98](https://github.com/bdvalero98)
